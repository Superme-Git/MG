<?php include APPPATH . "views/_header.php";?>
<?php include APPPATH . "views/faq/_menu.php";?>


        <div id="page-wrapper">
            <div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header">记录列表</h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
            <div class="row">
                <div class="col-md-12">
                    <div class="panel panel-default">
                    	<div class="panel-heading">选择分表</div>
                    	<div class="panel-body">
                    		<div class="col-md-6">
	                    		<div class="form-group">
		                    		<a href="/faq/record/index/" class="btn <?php echo !$cur_month ? 'btn-danger' : ''?>">默认</a>
		                    		<?php foreach ($recordSubtableList as $item){?>
		                    		<a href="/faq/record/index/<?php echo $item['key']?>" class="btn <?php echo $cur_month == $item['key'] ? 'btn-danger' : ''?>"><?php echo $item['name'].'('.$item['key'].')'?></a>
		                    		<?php }?>
	                    		</div>
                    		</div>
                    	</div>
                    	
                    	<div class="panel-heading">
                            DataTables Tables
                        </div>
                    	<div class="panel-body">
                    		<table class="table table-striped table-bordered table-hover">
							<thead>
								<tr class="success">
									<th>ID</th>
									<th>用户标志</th>
									<th>关键词</th>
									<th>FAQID</th>
									<th>反馈结果</th>
									<th>月份</th>
									<th>创建时间</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody>
								<?php if ($list){foreach ($list as $item){?>
								<tr class="odd ">
									<td><?php echo $item['record_id']?></td>
									<td><?php echo $item['userkey']?></td>
									<td><?php echo $item['keyword']?></td>
									<td><?php echo $item['answer_faq_id']?></td>
									<td><?php echo $item['feedback_result']?></td>
									<td><?php echo $item['month']?></td>
									<td class="center"><?php echo date('Y-m-d H:i:s', $item['create_time'])?></td>
									<td></td>
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
