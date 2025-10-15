<?php include APPPATH . "views/_header.php";?>
<?php include APPPATH . "views/_menu.php";?>

        <div id="page-wrapper">
            <div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header">头像列表</h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            
            <?php include APPPATH ."views/_alert.php";?>
            
            <!-- /.row -->
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">服务器列表</div>
                    	<div class="panel-body" >
                    	   <?php if ($game_list){foreach ($game_list as $item){?>
                    	   <a href="/role/index/<?php echo $item['gameid']?>" class="btn <?php echo $item['gameid'] == $game_id ? 'btn-primary' : ''?>"><?php echo $item['name']?></a>
                    	   <?php }}?>
                    	</div>
    					<div class="panel-heading"></div>
                    	<div class="panel-body">
                    		<table class="table table-striped table-bordered table-hover">
							<thead>
								<tr class="success">
									<th width="5%">ID</th>
									<th width="5%">游戏ID</th>
									<th width="10%">平台ID</th>
									<th width="5%">服务器ID</th>
									<th width="5%">头像</th>
									<th width="5%">录入时间</th>
									<th width="5%">操作</th>
								</tr>
							</thead>
							<tbody>
								<?php if ($list){foreach ($list as $item){?>
								<tr class="odd ">
									<td><?php echo $item['role_id']?></td>
									<td><?php echo $item['gameid']?></td>
									<td><?php echo $item['userid']?></td>
									<td><?php echo $item['serverid']?></td>
									<td><?php echo $item['icon']?></td>
									<td class="center"><?php echo date('Y-m-d', $item['create_time'])?></td>
									<td>
									</td>
								</tr>
								<?php }?>
								<tr class="odd">
									<td colspan="9">
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
									<td colspan="9" class="center">暂无结果</td>
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
