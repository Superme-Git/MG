<?php include APPPATH . "views/_header.php";?>
<?php include APPPATH . "views/_menu.php";?>


        <div id="page-wrapper">
            <div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header">服务器列表</h1>
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
					   <?php if (check_auth('root')){?>
						<a href="/serverlist/add" class="btn">添加服务器</a>
						<a href="/serverlist/merge" class="btn">合服</a>
						<a href="/serverlist/merge_list" class="btn">合服回滚</a>
						<?php }?>
					</div>
				</div>
                
                    <div class="panel panel-default">
                    	<div class="panel-heading"></div>
                    	<div class="panel-body">
                    		<table class="table table-striped table-bordered table-hover">
							<thead>
								<tr class="success">
									<th width="5%">ID</th>
									<?php if (check_auth('root')){?>
									<th width="10%">区服标识</th>
									<?php }?>
									<th width="10%">服务器名称</th>
									<?php if (check_auth('root')){?>
									<th width="5%">状态</th>
									<th width="7%">数据库状态</th>
									<th width="10%">创建时间</th>
									<?php }?>
									<th width="15%">操作</th>
								</tr>
							</thead>
							<tbody>
								<?php if ($list){foreach ($list as $item){?>
								<tr class="odd ">
									<td rowspan="2"><?php echo $item['serverlist_id']?></td>
									<?php if (check_auth('root')){?>
									<td><?php echo $item['serverkey']?></td>
									<?php }?>
									<td><?php echo $item['servername']?></td>
									<?php if (check_auth('root')){?>
									<td><?php echo !$item['status'] ? '可用' : '不可用'?></td>
									<td><?php echo !$item['db_status'] ? '未初始化' : '已初始化'?></td>
									<td class="center"><?php echo date('Y-m-d', $item['create_time'])?></td>
									<?php }?>
									<td>
									   <?php if(!$item['status']){?>
									    <?php if (!$item['db_status']){?>
									    <?php if (check_auth('root')){?>
									    <a href="/serverlist/db_init?serverkey=<?php echo $item['serverkey']?>" class="btn btn-warning">初始化数据库</a>
									    <?php }?>
									    <?php }else{?>
										<a href="/serverlist/select?serverlist_id=<?php echo $item['serverlist_id']?>" class="btn btn-success">进入</a>
										<?php }?>
										
										<?php if (check_auth('root')){?>
										<a href="/serverlist/edit?serverlist_id=<?php echo $item['serverlist_id']?>" class="btn btn-primary">编辑</a>
										<a href="javascript:;" onclick="return redirect_confirm('/serverlist/del?serverlist_id=<?php echo $item['serverlist_id']?>')" class="btn btn-danger">删除</a>
									    <?php }?>
									   <?php }?>
									</td>
								</tr>
								<tr class="odd ">
								    <td colspan="6"><?php echo $item['mark']?></td>
								</tr>
								<?php }?>
								<tr class="odd">
									<td colspan="7">
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
									<td colspan="7" class="center">暂无结果</td>
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
