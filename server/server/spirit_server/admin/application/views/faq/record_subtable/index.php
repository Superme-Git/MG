<?php include APPPATH . "views/_header.php";?>
<?php include APPPATH . "views/faq/_menu.php";?>

    <div id="page-wrapper">
		<div class="row">
			<div class="col-md-12">
				<h1 class="page-header">记录分表管理</h1>
			</div>
			<!-- /.col-md-12 -->
		</div>
		<?php include APPPATH ."views/_alert.php";?>
		<!-- /.row -->
        <div class="row">
        	<div class="col-md-12">
        		
        		<div class="panel panel-default">
					<div class="panel-body">
						<a href="<?php echo site_url('faq/record_subtable/index')?>" class="btn btn-success" >分表管理</a>
						<a href="<?php echo site_url('faq/record_subtable/add')?>" class="btn btn-default" >创建水平分表</a>
					</div>
				</div>
				
                    <div class="panel panel-default">
                    	<div class="panel-heading">数据列表</div>
                    	<table class="table table-striped table-bordered table-hover">
							<thead>
								<tr class="success">
									<th>ID</th>
									<th>KEY</th>
									<th>名称</th>
									<th>数据量</th>
									<th>排序</th>
									<th>时间</th>
									<th></th>
								</tr>
							</thead>
							<tbody>
								<?php if ($list){foreach ($list as $item){?>
								<tr class="odd ">
									<td><?php echo $item['record_subtable_id']?></td>
									<td><?php echo $item['key']?></td>
									<td><?php echo $item['name']?></td>
									<td><?php echo $item['count']?></td>
									<td><?php echo $item['sort']?></td>
									<td><?php echo $item['create_time']?></td>
									<td>
										<a href="javascript:;" onclick="return redirect_confirm('<?php echo site_url('faq/record_subtable/transfer/'.$item['key'])?>')">转移数据</a>
										<a href="javascript:;" onclick="return redirect_confirm('<?php echo site_url('faq/record_subtable/edit/'.$item['record_subtable_id'])?>')">编辑</a>
										<a href="javascript:;" onclick="return redirect_confirm('<?php echo site_url('faq/record_subtable/del/'.$item['record_subtable_id'])?>')">删除</a>
									</td>
								</tr>
								<?php }?>
								<?php }else{?>
								<tr class="odd ">
									<td colspan="7" class="center">暂无结果</td>
								</tr>
								<?php }?>
							</tbody>
						</table>
                    </div>
                    
                </div>
        </div>
    </div>

<?php include APPPATH . "views/_footer.php";?>