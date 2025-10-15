<?php include APPPATH . "views/_header.php";?>
<?php include APPPATH . "views/_menu.php";?>


        <div id="page-wrapper">
            <div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header">合服记录列表</h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <?php include APPPATH ."views/_alert.php";?>
            
            <!-- /.row -->
            <div class="row">
                <div class="col-lg-12">
                
                <div class="panel panel-default">
					<div class="panel-heading"></div>
					<div class="panel-body">
						<a href="/serverlist/add" class="btn">添加服务器</a>
						<a href="/serverlist/merge" class="btn">合服</a>
						<a href="/serverlist/merge_list" class="btn">合服回滚</a>
					</div>
				</div>
                
                    <div class="panel panel-default">
                    	<div class="panel-heading">
                            DataTables Tables
                        </div>
                    	<div class="panel-body">
                    		<table class="table table-striped table-bordered table-hover">
							<thead>
								<tr class="success">
									<th>ID</th>
									<th>合并源服务器</th>
									<th>目标服务器</th>
									<th>状态</th>
									<th>创建时间</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody>
								<?php if ($list){foreach ($list as $item){?>
								<tr class="odd ">
									<td><?php echo $item['server_merge_id']?></td>
									<td><?php echo implode(',', unserialize(base64_decode_($item['from_server_keys'])))?></td>
									<td><?php echo $item['to_server_key']?></td>
									<td><?php echo !$item['status'] ? '正常' : '已回滚'?></td>
									<td class="center"><?php echo date('Y-m-d H:i:s', $item['create_time'])?></td>
									<td>
									   <?php if(!$item['status']){?>
										<a href="/serverlist/merge_rollback?server_merge_id=<?php echo $item['server_merge_id']?>" class="btn btn-success">回滚</a>
									   <?php }?>
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
