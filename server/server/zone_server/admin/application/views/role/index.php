<?php include APPPATH . "views/_header.php";?>
<?php include APPPATH . "views/_menu_serverlist.php";?>

        <div id="page-wrapper">
            <div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header">玩家信息列表</h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            
            <?php include APPPATH ."views/_alert.php";?>
            
            <!-- /.row -->
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">筛选条件</div>
    					<div class="panel-body">
    						<div class="form-group">
    							<a href="/role/index/0" class="btn <?php echo !$avatar_verify ? 'btn-primary' : ''?>">未审核</a>
    							<a href="/role/index/1" class="btn <?php echo $avatar_verify == 1 ? 'btn-primary' : ''?>">全部</a>
    						</div>
    					</div>
    					<div class="panel-heading"></div>
                    	<div class="panel-body">
                    		<table class="table table-striped table-bordered table-hover">
							<thead>
								<tr class="success">
									<th width="5%">ID</th>
									<th width="5%">游戏名</th>
									<th width="5%">职业</th>
									<th width="5%">等级</th>
									<th width="10%">签名</th>
									<th width="5%">消息数</th>
									<th width="5%">人气数</th>
									<th width="5%">收礼物数</th>
									<th width="5%">录音地址</th>
									<th width="5%">头像</th>
									<th width="5%">状态</th>
									<th width="5%">审核时间</th>
									<th width="5%">操作</th>
								</tr>
							</thead>
							<tbody>
								<?php if ($list){foreach ($list as $item){?>
								<tr class="odd ">
									<td><?php echo $item['roleid']?></td>
									<td><?php echo $item['name']?></td>
									<td><?php echo $item['avatar']?></td>
									<td><?php echo $item['level']?></td>
									<td><?php echo $item['signature']?></td>
									<td><?php echo $item['msg_count']?></td>
									<td><?php echo $item['popularity_count']?></td>
									<td><?php echo $item['gift_count']?></td>
									<td><?php if ($item['sound_url']){?><audio controls="controls"><source src="<?php echo $item['sound_url']?>" ></audio><?php }?></td>
									<td><?php if ($item['avatar_url']){?><a href="javascript:;" onclick="alert_img('<?php echo $item['avatar_url']?>')" ><img src="<?php echo $item['avatar_url']?>" style="height:100px" /></a><?php }?></td>
									<td class="center"><?php echo $item['avatar_verify']?></td>
									<td class="center"><?php echo $item['avatar_verify_time'] ? date('Y-m-d', $item['avatar_verify_time']) : ''?></td>
									<td>
										<?php if (!$item['avatar_verify'] && $item['avatar_url']){?>
										<a href="/role/verify?roleid=<?php echo $item['roleid']?>&verify=1" class="btn btn-success">通过</a>
										<a href="/role/verify?roleid=<?php echo $item['roleid']?>&verify=2" class="btn btn-danger">不通过</a>
										<?php }elseif ($item['avatar_verify'] && $item['avatar_url']){?>
										<a href="/role/verify?roleid=<?php echo $item['roleid']?>&verify=2" class="btn btn-danger">复审不通过</a>
										<?php }?>
<!--										<a href="/index/edit?serverlist_id=<?php //echo $item['serverlist_id']?>" class="btn btn-primary">编辑</a>-->
										<!--<a href="javascript:;" onclick="return redirect_confirm('/index/del?serverlist_id=<?php echo $item['serverlist_id']?>')" class="btn btn-warning">复审不通过</a>-->
									</td>
								</tr>
								<?php }?>
								<?php }else{?>
								<tr class="odd ">
									<td class="center">暂无结果</td>
								</tr>
								<?php }?>
							</tbody>
							</table>
							
    						<table class="table table-striped table-bordered table-hover">
    						<tr class="success">
    						<td>
    						  <ul class="pagination">
    							<li class="disabled"><a href="#">显示 <?php echo $pagination['now'];?> 条</a></li>
    							<li class="disabled"><a href="#">总共：<?php echo $pagination['total'];?> 条</a></li>
    							</ul>
    							<ul class="pagination">
    							<?php echo $pagination['pages']?>
    							</ul>
    						</td>
    						</tr>
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
