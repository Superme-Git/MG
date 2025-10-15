<?php include APPPATH . "views/_header.php";?>
<?php include APPPATH . "views/faq/_menu.php";?>


        <div id="page-wrapper">
            <div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header">FAQ列表</h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                    	<div class="panel-heading">
                            DataTables Tables
                        </div>
                        
                        <div class="panel-body">
                    		<div class="row">
                    		<div class="col-md-12">
                    		<div class="form-group">
	                    		<a href="/faq/faq/index/" class="btn <?php echo !$cur_category_id ? 'btn-danger' : ''?>">全部</a>
	                    		<?php foreach ($categoryList as $item){?>
	                    		<a href="/faq/faq/index/<?php echo $item['category_id']?>/0" class="btn <?php echo $cur_category_id == $item['category_id'] ? 'btn-danger' : ''?>"><?php echo $item['name']?></a>
	                    		<?php }?>
                    		</div>
                    		</div>
                    		</div>
                    	</div>
                        
                    	<div class="panel-body">
                    		<?php include APPPATH ."views/_alert.php";?>
                    		<table class="table table-striped table-bordered table-hover">
							<thead>
								<tr class="success">
									<th width="5%">ID</th>
									<th width="10%">标题</th>
									<th width="5%">分类</th>
									<th width="10%">关键词</th>
									<th width="5%">排序</th>
									<th width="10%">创建时间</th>
									<th width="15%">操作</th>
								</tr>
							</thead>
							<tbody>
								<?php if ($list){foreach ($list as $item){?>
								<tr class="odd ">
									<td><?php echo $item['faq_id']?></td>
									<td><?php echo $item['title']?></td>
									<td><?php echo $item['category_info'] ? $item['category_info']['name'] : ''?></td>
									<td><?php echo $item['keywords']?></td>
									<td><?php echo $item['sort']?></td>
									<td class="center"><?php echo date('Y-m-d H:i:s', $item['create_time'])?></td>
									<td>
										<a href="/faq/faq/edit?faq_id=<?php echo $item['faq_id']?>" class="btn btn-default">编辑</a>
										<a href="javascript:;" onclick="return redirect_confirm('/faq/faq/del?faq_id=<?php echo $item['faq_id']?>')" class="btn btn-default" >删除</a>
									</td>
								</tr>
								<?php }?>
								<tr class="odd">
									<td colspan="8">
										<ul class="pagination">
										<li class="disabled"><a href="#">显示 <?php echo $pagination['now'];?> 条</a></li>
										<li class="disabled"><a href="#">总共：<?php echo $pagination['total'];?> 条</a></li>
										</ul>
										<ul class="pagination">
										<?php echo $pagination['pages']?>
										</ul>
									</td>
								</tr>
								<?php }else{?>
								<tr class="odd ">
									<td colspan="8" class="center">暂无结果</td>
								</tr>
								<?php }?>
							</tbody>
						</table>
						
                        </div>
                    </div>
                    <!-- /.panel -->
                    
                </div>
               
            </div>
            <!-- /.row -->
        </div>
        <!-- /#page-wrapper -->
    

    <?php include APPPATH . "views/_footer.php";?>
