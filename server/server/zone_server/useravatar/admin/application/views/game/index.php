<?php include APPPATH . "views/_header.php";?>
<?php include APPPATH . "views/_menu.php";?>


        <div id="page-wrapper">
            <div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header">游戏列表</h1>
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
						<a href="/game/add" class="btn">添加游戏</a>
					</div>
				</div>
                
                    <div class="panel panel-default">
                    	<div class="panel-heading"></div>
                    	<div class="panel-body">
                    		<table class="table table-striped table-bordered table-hover">
							<thead>
								<tr class="success">
									<th width="5%">ID</th>
									<th width="10%">游戏ID</th>
									<th width="10%">游戏名</th>
									<th width="7%">表状态</th>
									<th width="10%">创建时间</th>
									<th width="15%">操作</th>
								</tr>
							</thead>
							<tbody>
								<?php if ($list){foreach ($list as $item){?>
								<tr class="odd ">
									<td rowspan="2"><?php echo $item['game_id']?></td>
									<td><?php echo $item['gameid']?></td>
									<td><?php echo $item['name']?></td>
									<td><?php echo !$item['table_status'] ? '未初始化' : '已初始化'?></td>
									<td class="center"><?php echo date('Y-m-d', $item['create_time'])?></td>
									<td>
									    <?php if (!$item['table_status']){?>
									    <a href="/game/table_init?gameid=<?php echo $item['gameid']?>" class="btn btn-warning">初始化表</a>
									    <?php }?>
										<a href="/game/edit?serverlist_id=<?php echo $item['serverlist_id']?>" class="btn btn-primary">编辑</a>
										<a href="javascript:;" onclick="return redirect_confirm('/game/del/<?php echo $item['game_id']?>')" class="btn btn-danger">删除</a>
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
