#include <stdlib.h>
#include <stdarg.h>
#include <time.h>

#define private public
#include "db.h"

using namespace GNET;
using __db_helper::PageLayout;
using __db_helper::page_pos_t;
using __db_helper::frag_hdr;
using __db_helper::index_hdr;
using __db_helper::data_hdr;
using __db_helper::logger_hdr;
using __db_helper::Page;
using __db_helper::IQueryData;
using __db_helper::time32_t;
using __db_helper::size32_t;


static bool verify(bool condition, const char* fmt, ...)
{
    if (condition)  return true;
    va_list ap;
    va_start(ap, fmt);
    vfprintf(stderr, fmt, ap);
    va_end(ap);
    return false;
}

static void assert(bool condition, const char* fmt, ...)
{
    if (condition)  return;
    va_list ap;
    va_start(ap, fmt);
    vfprintf(stderr, fmt, ap);
    va_end(ap);
    exit(1);
}

static char* hexstr( const void *src, size_t len)
{
	char *dst = (char *)malloc(2*len+1);
	assert(dst!=NULL, "hexstr malloc err\n");
	const unsigned char* s = (const unsigned char *)src;
	for(int i=0; i<len; i++)
		sprintf(dst+2*i, "%02x", s[i]);
	dst[2*len] = '\0';
	return dst;
}

static char* timestr(time32_t t)
{
    static char buf[64];
    time_t tt = t;
    struct tm *tmp = localtime(&tt);
    assert(NULL!=tmp, "localtime err\n");
    strftime(buf, 64, "%Y-%m-%d-%H:%M:%S", tmp);
    return buf;
}

static char* index_hdr_str(index_hdr * ih)
{
    static char buf[256] ;
    char *key = hexstr(ih->key, ih->key_len);
    snprintf(buf, 256, "[child_index: %4d, page: (%4d,%4d), key_len: %3d, key: %s]", 
            ih->child_index, ih->page_index, ih->page_pos, ih->key_len, key);
    free(key);
    return buf;
}

static char* logger_hdr_str( logger_hdr *hdr) 
{
    static char buf[256] ;
    snprintf(buf, 256, "[logger_idx: [%4d,%4d), check_timestamp: %10d %s, check_result: %d]", 
            hdr->logger_first_idx, hdr->logger_last_idx, hdr->logger_check_timestamp, 
            timestr(hdr->logger_check_timestamp), hdr->check_result);
    return buf;
}

static char* indent_str(int indent)
{
    static char buf[256];
    buf[0] = '\0';
    for(int i=0; i<indent && i<256/4; i++)
        strcat(buf, "-   ");
    return buf;
}


class StateMap
{
    char    *map;
    size_t  map_size;
    size_t  rangeerr;
    size_t  alignerr;
    size_t  mixerr;

public:

    ~StateMap(){ if (map) free(map); }

    #define PIECESIZE 16
    #define PIECECNT_PER_PAGE (PAGESIZE/PIECESIZE)
    enum State { NIL='n', DATA='d', FRAG='f', INDEX='i', INDEX_HOLE='!', PAGE='p', MAGIC='m', LOG_MAGIC='L', LOG='l', MIXED='x' };

    StateMap(size_t page_cnt)
	{
        map_size = PIECECNT_PER_PAGE  * page_cnt;
        map = (char *)malloc(map_size); 
        assert(map, "malloc state.map err, fixme\n");
        memset(map, NIL, map_size);
        rangeerr = alignerr = mixerr = 0;
    }

    void set_page(page_index_t page, page_pos_t from, page_pos_t to,  State state)
    {
        assert(state != NIL, "set [%d,%d,%d] state nil", page, from, to);
        if (!verify( from<to && to <=PAGESIZE, "[%d,%d,%d] rangeerr\n", page,from,to ))  
            rangeerr++;
        if (!verify( 0==(from%PIECESIZE) && (to==PAGEUSED || 0==to%PIECESIZE), "[%d,%d,%d] alignerr\n", page,from,to ))  
            alignerr++;

        size_t f = page * PIECECNT_PER_PAGE + from / PIECESIZE;
        size_t t = page * PIECECNT_PER_PAGE + (to + PIECESIZE-1) / PIECESIZE;
        for(size_t i=f; i<t; i++){
            if (!verify(map[i] == NIL, "[%d,%d,%d] mixed, old: %c, new: %c\n", page, from, to, map[i], state)){
                mixerr++;
                map[i] = MIXED;
            }
            else{
                map[i] = state;
            }
        }
    }

    void dump()
    {
        size_t nilcnt = 0;
        size_t mixcnt = 0;
        for(int i=0; i<map_size; i++){
            if ( NIL == map[i])     nilcnt++;
            if ( MIXED == map[i])   mixcnt++;
        }
        verify(0==nilcnt && 0==mixcnt, "statemap nilcnt=%d, mixcnt=%d\n", nilcnt, mixcnt);
        printf("rangeerr: %d, alignerr: %d, mixerr: %d, nilcnt: %d, mixcnt: %d\n", rangeerr, alignerr, mixerr, nilcnt, mixcnt);
    }

    void save(const char *fn)
    {
        size_t nilcnt = 0;
        size_t mixcnt = 0;
        for(int i=0; i<map_size; i++){
            if ( NIL == map[i])     nilcnt++;
            if ( MIXED == map[i])   mixcnt++;
        }
        
        FILE *fp = fopen(fn, "w");
        fprintf(fp, "# rangeerr: %d, alignerr: %d, mixerr: %d, nilcnt: %d, mixcnt: %d\n", 
                rangeerr, alignerr, mixerr, nilcnt, mixcnt);
        fprintf(fp, "# states: n: NIL, d: DATA, f: FRAG, i: INDEX, !: INDEX_HOLE, p: PAGE, m: MAGIC, L: LOG_MAGIC, l: LOG, x: MIXED\n");
        fprintf(fp, "# 16 byte per state, 256 state per line, only indexpage flaged p at end\n");
        for(int i=0; i<map_size/PIECECNT_PER_PAGE; i++){
            fwrite(map+i*PIECECNT_PER_PAGE, PIECECNT_PER_PAGE, 1, fp);
            fwrite("\n", 1, 1, fp);
        }
        fclose(fp);
    }
};


class Dumper
{
    PageCache *page_cache;
    bool verbose;
    char* dbfile;
    StateMap *statemap;

    void statemap_index_hdr_data(index_hdr * ih)
    {
        page_index_t pi = ih->page_index;
        page_pos_t   pp = ih->page_pos;
        while(pi){
            data_hdr *hdr = (data_hdr *)page_cache->Rload_hdr(pi, pp);
            statemap->set_page(pi, pp, hdr->next, StateMap::DATA);
            //printf("%d->", hdr->next - pp);
            pi = hdr->next_page_index;
            pp = hdr->next_page_pos;
        }
    }

    
    bool is_data_page(data_hdr* hdr)
	{
        data_hdr *cur = hdr;
		for ( int i = 0; i < PAGEUSED / 16; i++ ){
			cur = cur->next_head();
			if ( !cur ) return true;
			ptrdiff_t offset = (char *)cur - (char *)hdr;
			if ( offset > PAGEUSED || offset < 0 ) return false;
		}
		return false;
    }

public:
    ~Dumper()
	{
        if (page_cache) delete page_cache;
        if (dbfile) free(dbfile);
    }

    Dumper(const char* db, size_t max = 8192, bool verb=true)
	{
        dbfile = strdup(db);
	    page_cache = new PageCache(new PageFile( db ), max, max/2);
        verbose = verb;
        statemap = NULL;
    }

    void dump_data_page(page_index_t idx)
    {
        data_hdr *hdr = (data_hdr *)page_cache->Rload_hdr(idx, 0);
        if (!verify(is_data_page(hdr), "%d is not data page\n", idx))
            return;

        while(hdr){
            if (hdr->size)
                printf("size: %4d, next: %4d, first_slice=%d\n", hdr->size, hdr->next, hdr->first_slice);
            else 
                printf("size: %4d, next: %4d\n", hdr->size, hdr->next);
            hdr = hdr->next_head();
        }
    }

	void dump_index_page(page_index_t page_index, int indent=0, bool recursion=false)
	{
		Page* page = page_cache->Rload(page_index);
		PageLayout* pl = page->layout_ptr();
		index_hdr* end = page->index_end();
		verify (end->l_pos < end->r_pos,  "%d-end l_pos >= r_pos", page_index);
		verify (end->r_pos <= INDEXCOUNT, "%d-end r_pos > INDEXCOUNT", page_index);

		if (verbose || indent == 0){
			printf("%s%4d> [child_index: %4d, parent: (%4d,%4d), l_pos: %4d, r_pos: %4d]\n", 
				indent_str(indent), page_index, end->child_index, end->parent_index, end->parent_pos, 
				end->l_pos, end->r_pos);
		}
		if (statemap){
			statemap->set_page(page_index, INDEXCOUNT*sizeof(index_hdr), (INDEXCOUNT+1)*sizeof(index_hdr), StateMap::INDEX);
			statemap->set_page(page_index, (INDEXCOUNT+1)*sizeof(index_hdr), PAGESIZE, StateMap::PAGE);
		}
		for(int i=0; i<INDEXCOUNT; i++){
			index_hdr* ih = pl->index + i;
			bool index_hole = !( end->l_pos <= i && i <= end->r_pos );
			if ( index_hole ){
				if (statemap)
					statemap->set_page(page_index, i*sizeof(index_hdr), (i+1)*sizeof(index_hdr), StateMap::INDEX_HOLE);
				continue;
			}
			bool r_pos = (i == end->r_pos);
			if ( r_pos && 0 == ih->child_index ){ //r_pos leaf node is hole
				if (statemap)
					statemap->set_page(page_index, i*sizeof(index_hdr), (i+1)*sizeof(index_hdr), StateMap::INDEX_HOLE);
				continue;
			}

			if(statemap){
				statemap->set_page(page_index, i*sizeof(index_hdr), (i+1)*sizeof(index_hdr), StateMap::INDEX);
				if ( !r_pos ) 
					statemap_index_hdr_data(ih);
				else //r_pos internal node 's data is unused. although it's child_index is used as leftnode's right child.
					;
			}
			if (verbose || indent == 0) 
				printf("%s %3d: %s\n", indent_str(indent), i, index_hdr_str(ih));

			if ( ih->child_index && recursion )
				dump_index_page( ih->child_index, indent+1, recursion );
		}
	}

    void dump()
    {
        if (statemap){ delete statemap; statemap = NULL; }

        struct stat st;
        assert( 0 == stat(dbfile, &st), "stat %s err\n", dbfile);

        size_t size  = st.st_size;
        size_t pagec = size / PAGESIZE;

        assert( 0 != pagec, "stat pagec zero\n");
        verify( 0 == size%PAGESIZE, "stat filesize not align pagesize\n");

        char *p = strrchr(dbfile, '/');
        char *ident = strdup(p? p+1 : dbfile);
        //global logger file
        if ( strlen(ident) > 4 && strncmp( ident, "log.", 4) == 0){
            time32_t ts;
            sscanf(ident, "log.%x", &ts);
            PageLayout* pl = page_cache->magic_layout_ptr();
            
            puts("------------------------------");
            printf("%s: global logger file\n", dbfile);
            puts("------------------------------");
            puts("magic:");
            printf("logger_page_first            : %u\n", pl->logger_page_first);
            printf("logger_page_last             : %u\n", pl->logger_page_last);
            printf("logger_rec_max               : %u\n", pl->logger_rec_max);
            printf("logger_rec_cur               : %u\n", pl->logger_rec_cur);
            printf("logger_chain                 : %u\n", pl->logger_chain);

            puts("------------------------------");
            puts("logger_head:");
            for(size_t i=0; i<=pl->logger_rec_cur; i++)
                printf("%3d: %s\n", i, logger_hdr_str( pl->logger_head + i));
            
            for(size_t i=1; i<pl->logger_page_first; i++){
                char* layout = (char *)page_cache->Rload_hdr(i, 0);
                size32_t offset = *(size32_t *)layout;
                verify(offset%16 == 0, "global logger_hdr not aligned\n");
                char* name = layout + sizeof(size32_t);
                printf("%3d %s:\n", i, name);
                if (verbose){
                    logger_hdr* hdr = (logger_hdr*)(layout + offset);
                    for(size_t j=0; j<=pl->logger_rec_cur; j++)
                        printf("   %3d: %s\n", j, logger_hdr_str( hdr + j));
                }
            }

            return;
        }

        //data file
        PageLayout* pl = page_cache->magic_layout_ptr();
        page_index_t logger_magic_idx = pl->logger_magic_idx;
	    statemap = new StateMap( pagec );
		statemap->set_page(0, 0, PAGESIZE, StateMap::MAGIC);

		char *dbfiletype = "dbfile";
        if ( logger_magic_idx ){
            verify(logger_magic_idx+1 == pagec, "logger_magic_idx+1 != filesize/pagesize");
            verify(pl->logger_first_idx <= logger_magic_idx, "logger_first idx > logger_magic_idx");
			dbfiletype = "dbfile with pagelog, required redo";
            for (page_index_t i=pl->logger_first_idx; i < logger_magic_idx; i++)
                statemap->set_page(i, 0, PAGESIZE, StateMap::LOG);
            statemap->set_page( logger_magic_idx, 0, PAGESIZE, StateMap::LOG_MAGIC);
        }else
            verify(pl->max_page_idx+1 == pagec, "logger_magic_idx+1 != filesize/pagesize");
		
		puts("------------------------------");
		printf("%s: %s\n", dbfile, dbfiletype);
		puts("------------------------------");
		puts("magic:");
        printf("free_page_list               : %u\n", pl->free_page_list);
        printf("max_page_idx                 : %u\n", pl->max_page_idx);
        printf("root_index_idx               : %u\n", pl->root_index_idx);
        printf("logger_last_check            : %u %s\n", pl->logger_last_check, timestr(pl->logger_last_check) );
        printf("logger_first_idx             : %u\n", pl->logger_first_idx);
        printf("logger_magic_idx             : %u\n", pl->logger_magic_idx);
        printf("logger_id                    : %u %s\n", pl->logger_id, timestr(pl->logger_id));
        printf("logger_serial                : %u\n", pl->logger_serial);
        puts("------------------------------");
        puts("performance:");
        pl->performance.dump();

        puts("------------------------------");
        puts("frags:");
        for(int i=0; i<PAGEUSED/FRAGMIN; i++){
            page_index_t ni = pl->frag_page_index[i];
            page_pos_t   np = pl->frag_page_pos[i];
            verify( !(ni == 0 && np != 0), "frag %d page_index=0 but page_pos!=0\n", i);
            if ( !ni ) continue;

            printf("%4d : ", i*FRAGMIN);
            page_index_t pi = 0;
            page_pos_t pp = 0;
            int s = 0;
            while(ni){
                frag_hdr *hdr = (frag_hdr *)page_cache->Rload_hdr(ni, np);
                verify (hdr->size == 0, "frag [%u, %u] size not zero\n", ni, np);
                verify (pi == hdr->prev_page_index && pp == hdr->prev_page_pos, "frag [%u, %u] prev mismatch\n", ni, np);

                if (s && s % 5 == 0 && verbose)
                    printf("\n       ");
                if (verbose || s == 0)
                    printf("-> [%u,%u,%u] ", ni,  np, hdr->next);
                s++;

                statemap->set_page(ni, np, hdr->next, StateMap::FRAG);
                pi = ni;
                pp = np;
                ni = hdr->next_page_index;
                np = hdr->next_page_pos;
            }
            printf("\n       (sum=%d)\n", s);
        }

        if (page_index_t n = pl->free_page_list){
            printf("free_page : ");
            int s = 0;
            while(n){
                if ( s && s % 5 == 0 && verbose)
                    printf("\n            ");
                if (verbose || s == 0)
                    printf("-> [%d]", n);
				s++;

                statemap->set_page(n, 0, PAGESIZE, StateMap::FRAG);
                Page * page = page_cache->Rload(n);
                n = page->get_free_page_list();
            }
            printf("\n            (sum=%d)\n", s);
        }

        puts("------------------------------");
        puts("index:");
        dump_index_page( pl->root_index_idx, 0, true);

        puts("------------------------------");
        puts("statemap:");
		char *dbmapfile = (char *)malloc(strlen(dbfile) + 5);
		strcpy(dbmapfile, dbfile);
		strcat(dbmapfile, ".map");
		statemap->save(dbmapfile);
		statemap->dump();
        printf("saved at %s\n", dbmapfile);

    }
};


class MyQuery : public IQueryData
{
public:
	~MyQuery() { }
	bool update( const void *key, size_t key_len, const void *val, size_t val_len ) 
	{
		char *keyhex = hexstr(key, key_len);
		printf("key: %s, val_len: %d\n", keyhex, val_len);
       	free(keyhex);
        return true;
	}
};


void usage()
{
    puts("usage: dbcheck [-v]                   verbose, used with -c, dump all frag links and index btree");
    puts("               [-c <dbfile>]          check dbfile, dump info to stdout, err to stderr");
    puts("               [-c <dbfile> -w]       walk by index, dump all key and value_len");
    puts("               [-c <dbfile> -i <idx>] dump index_hdr at <idx> as indexpage");
    puts("               [-c <dbfile> -d <idx>] dump data_hdr  at <idx> as datapage");
    exit(0);
}

int main(int argc, char** argv)
{
    bool verbose = false;
    bool walk = false;
    char *db = NULL;
    int  iidx = 0;
    int  didx = 0;

    int opt;
    while ((opt = getopt(argc, argv, "hvwc:i:d:")) != -1) {
        switch (opt) {
        case 'h': usage();
        case 'v': verbose = true; break;
        case 'w': walk=true; break;
        case 'c': db = strdup(optarg); break;
        case 'i': iidx = atoi(optarg); break;
        case 'd': didx = atoi(optarg); break;
        default : usage();
        }
    }

    if (!db)
        usage();

    if (walk){
        (new PageCache(new PageFile(db), 8192, 4096))->walk(new MyQuery());
        return 0;
    }

    Dumper d = Dumper(db, 8192, verbose);
    if (iidx)
        d.dump_index_page(iidx);
    else if (didx)
        d.dump_data_page(didx);
    else
        d.dump();
    return 0;
}
