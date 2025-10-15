<?php include APPPATH . "views/_header.php";?>
<?php include APPPATH . "views/faq/_menu.php";?>

    <div id="page-wrapper">
		<div class="row">
			<div class="col-md-12">
				<h1 class="page-header">重建搜索引擎索引</h1>
			</div>
			<!-- /.col-md-12 -->
		</div>
		<?php include APPPATH ."views/_alert.php";?>
		<!-- /.row -->
        <div class="row">
        	<form id="edit_form" role="form" method="post" action="" class="form-horizontal">
        	<div class="col-md-6">
				<div class="panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title"></h3>
					</div>
					<div class="panel-body">
						<div class="col-md-12">
							<div class="form-group">
								<label class="control-label" for="title">确定重建搜索引擎索引?</label>
							</div>
							
						</div>
					</div>
				</div>
			</div>
			
			
			
			<div class="col-md-12">
				<div class="panel panel-default">
					<div class="panel-body">
						<div class="form-group">
							<div class="col-md-8">
								<button type="submit" class="btn btn-info">提交表单</button>
							</div>
						</div>
					</div>
				</div>
			</div>
			</form>
        </div>
    </div>


<?php include APPPATH . "views/_footer.php";?>