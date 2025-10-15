// Minimal stub implementation of OggEnc encode routines for Windows build.
// This restores missing symbols declared in encode.h to unblock FireClient.lib generation.
// NOTE: This is a placeholder that simulates encoding without producing real Ogg/Vorbis output.
// Replace with the original Application/oggenc/encode.cpp when available.

#include "encode.h"
#include "audio.h"
#include "platform.h"

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

static void safe_print(const char* s) {
    if (s && *s) {
        fputs(s, stdout);
    }
}

void update_statistics_full(char *fn, long total, long done, double time) {
    (void)time; // placeholder (no timing computation here)
    if (!fn) fn = (char*)"";
    fprintf(stdout, "[OggEnc] progress: file=\"%s\" %ld/%ld samples\n", fn, done, total);
    fflush(stdout);
}

void update_statistics_notime(char *fn, long total, long done, double time) {
    (void)time;
    if (!fn) fn = (char*)"";
    fprintf(stdout, "[OggEnc] progress: file=\"%s\" %ld/%ld samples (no time)\n", fn, done, total);
    fflush(stdout);
}

void update_statistics_null(char *fn, long total, long done, double time) {
    (void)fn; (void)total; (void)done; (void)time;
    // no-op
}

void start_encode_full(char *fn, char *outfn, int bitrate, float quality, int qset,
                       int managed, int min_br, int max_br) {
    if (!fn) fn = (char*)"";
    if (!outfn) outfn = (char*)"";
    fprintf(stdout,
            "[OggEnc] start: in=\"%s\" out=\"%s\" bitrate=%d quality=%.3f qset=%d "
            "managed=%d min=%d max=%d\n",
            fn, outfn, bitrate, quality, qset, managed, min_br, max_br);
    fflush(stdout);
}

void start_encode_null(char *fn, char *outfn, int bitrate, float quality, int qset,
                       int managed, int min_br, int max_br) {
    (void)fn; (void)outfn; (void)bitrate; (void)quality; (void)qset;
    (void)managed; (void)min_br; (void)max_br;
    // no-op
}

void final_statistics(char *fn, double time, int rate, long total_samples, long bytes) {
    if (!fn) fn = (char*)"";
    fprintf(stdout,
            "[OggEnc] final: file=\"%s\" time=%.3f rate=%d total_samples=%ld bytes=%ld\n",
            fn, time, rate, total_samples, bytes);
    fflush(stdout);
}

void final_statistics_null(char *fn, double time, int rate, long total_samples, long bytes) {
    (void)fn; (void)time; (void)rate; (void)total_samples; (void)bytes;
    // no-op
}

void encode_error(char *errmsg) {
    if (!errmsg) errmsg = (char*)"Unknown OggEnc error";
    fprintf(stderr, "[OggEnc] error: %s\n", errmsg);
    fflush(stderr);
}

// Minimal simulation of the oe_encode() pipeline:
// - Reads samples via opt->read_samples
// - Reports progress via opt->progress_update
// - Calls start_encode/end_encode callbacks
// - Optionally writes a small placeholder header to opt->out
int oe_encode(oe_enc_opt *opt) {
    if (!opt) {
        encode_error((char*)"Invalid encode options (opt == NULL)");
        return 1;
    }

    const int channels = (opt->channels > 0) ? opt->channels : 1;
    const int chunk = 4096;

    // Prepare per-channel float buffers
    float **bufs = (float**)malloc(sizeof(float*) * channels);
    if (!bufs) {
        encode_error((char*)"Out of memory allocating channel buffers");
        return 1;
    }
    for (int c = 0; c < channels; ++c) {
        bufs[c] = (float*)malloc(sizeof(float) * chunk);
        if (!bufs[c]) {
            encode_error((char*)"Out of memory allocating sample buffer");
            for (int i = 0; i < c; ++i) free(bufs[i]);
            free(bufs);
            return 1;
        }
    }

    // Announce start
    if (opt->start_encode) {
        opt->start_encode(
            opt->infilename ? opt->infilename : (char*)"",
            opt->filename ? opt->filename : (char*)"",
            opt->bitrate,
            opt->quality,
            opt->quality_set,
            opt->managed,
            opt->min_bitrate,
            opt->max_bitrate
        );
    }

    // Write a tiny placeholder header so output file is non-empty
    long bytes_written = 0;
    if (opt->out) {
        const char *placeholder = "OggEnc-Placeholder\n";
        size_t n = strlen(placeholder);
        if (fwrite(placeholder, 1, n, opt->out) == n) {
            bytes_written += (long)n;
        }
    }

    long total_done = 0;
    // Read loop
    if (opt->read_samples) {
        while (1) {
            long got = opt->read_samples(opt->readdata, bufs, chunk);
            if (got <= 0) break;
            total_done += got;

            // Report progress
            if (opt->progress_update) {
                opt->progress_update(
                    opt->filename ? opt->filename : (char*)"",
                    opt->total_samples_per_channel,
                    total_done,
                    0.0 /* no timing in stub */
                );
            }

            // In stub we do not actually encode; we can optionally bump bytes_written
            // to simulate output growth.
            if (opt->out) {
                // Write a block marker to simulate progress
                const char *blk = "blk\n";
                size_t bn = 4;
                if (fwrite(blk, 1, bn, opt->out) == bn) {
                    bytes_written += (long)bn;
                }
            }
        }
    } else {
        // No reader provided; treat as success with zero data
    }

    // Finalize
    if (opt->end_encode) {
        opt->end_encode(
            opt->filename ? opt->filename : (char*)"",
            0.0,                 // no timing measured in stub
            (int)opt->rate,
            total_done,
            bytes_written
        );
    }

    // Cleanup
    for (int c = 0; c < channels; ++c) {
        free(bufs[c]);
    }
    free(bufs);

    return 0; // success
}