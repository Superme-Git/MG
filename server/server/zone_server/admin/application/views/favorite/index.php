<?php include APPPATH . "views/_header.php";?>
<?php include APPPATH . "views/_menu_serverlist.php";?>

        <div id="page-wrapper">
            <div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header">微博点赞列表</h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <?php include APPPATH ."views/_alert.php";?>
            
            <!-- /.row -->
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                    	<div class="panel-heading">
                            DataTables Tables
                        </div>
                    	<div class="panel-body">
                    		<table class="table table-striped table-bordered table-hover">
							<thead>
								<tr class="success">
									<th>ID</th>
									<th>微博ID</th>
									<th>点赞玩家ID</th>
									<th>状态</th>
									<th>创建时间</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody>
								<?php if ($list){foreach ($list as $item){?>
								<tr class="odd ">
									<td><?php echo $item['status_favorite_id']?></td>
									<td><?php echo $item['status_id']?></td>
									<td><?php echo $item['roleid']?></td>
									<td><?php echo $item['status']?></td>
									<td class="center"><?php echo date('Y-m-d H:i:s', $item['create_time'])?></td>
									<td>
<!--										<a href="/index/select?serverlist_id=<?php //echo $item['serverlist_id']?>" class="btn btn-success">进入</a>-->
<!--										<a href="/index/edit?serverlist_id=<?php //echo $item['serverlist_id']?>" class="btn btn-primary">编辑</a>-->
<!--										<a href="javascript:;" onclick="return redirect_confirm('/index/del?serverlist_id=<?php //echo $item['serverlist_id']?>')" class="btn btn-warning">删除</a>-->
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
