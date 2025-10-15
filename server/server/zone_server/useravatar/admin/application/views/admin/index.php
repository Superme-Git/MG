<?php include __DIR__."/../_header.php";?>
<?php include __DIR__."/../_menu.php";?>

    <div id="page-wrapper">
		<div class="row">
			<div class="col-md-12">
				<h1 class="page-header">用户管理</h1>
			</div>
			<!-- /.col-md-12 -->
		</div>
		<?php include APPPATH ."views/_alert.php";?>
		<!-- /.row -->
        <div class="row">
        	<div class="col-md-12">
                    <div class="panel panel-default">
                    	<div class="panel-heading">
                            DataTables Tables
                        </div>
                    	<div class="panel-body">
                    		<table class="table table-striped table-bordered table-hover">
							<thead>
								<tr class="success">
									<th>用户ID</th>
									<th>用户邮箱</th>
									<th>创建时间</th>
									<th>操作</th>
									<th></th>
								</tr>
							</thead>
							<tbody>
								<?php if ($list){foreach ($list as $item){?>
								<tr class="odd ">
									<td><?php echo $item['admin_id']?></td>
									<td><?php echo $item['email']?></td>
									<td class="center"><?php echo date("Y-m-d H:i:s", $item['create_time'])?></td>
									<td>
										<a href="/admin/edit?admin_id=<?php echo $item['admin_id']?>">编辑</a>
									</td>
									<td></td>
								</tr>
								<?php }?>
								<tr class="odd">
									<td colspan="6">
										<ul class="pagination">
										<li class="disabled"><a href="#">显示 <?php echo $pagination['now'];?> 条</a></li>
										<li class="disabled"><a href="#">总共：<?php echo $pagination['total'];?> 页</a></li>
										</ul>
										<ul class="pagination">
										<?php echo $pagination['pages']?>
										</ul>
									</td>
								</tr>
								<?php }else{?>
								<tr class="odd ">
									<td colspan="6" class="center">暂无结果</td>
								</tr>
								<?php }?>
							</tbody>
						</table>
						
                        </div>
                    </div>
                    <!-- /.panel -->
                    
                </div>
        </div>
    </div>

<?php include APPPATH . "views/_footer.php";?>