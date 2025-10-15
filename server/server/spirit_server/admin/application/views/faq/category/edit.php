<?php include APPPATH . "views/_header.php";?>
<?php include APPPATH . "views/faq/_menu.php";?>

    <div id="page-wrapper">
		<div class="row">
			<div class="col-md-12">
				<h1 class="page-header">编辑分类</h1>
			</div>
			<!-- /.col-md-12 -->
		</div>
		<?php include APPPATH ."views/_alert.php";?>
		<!-- /.row -->
        <div class="row">
        	<form id="add_form" method="post" action="" class="form">
        	<div class="col-md-6">
				<div class="panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title">填写基本信息</h3>
					</div>
					<div class="panel-body">
						<div class="col-md-12">
							<div class="form-group">
								<label class="control-label" for="name">名称</label>
								<input type="text" class="form-control" id="name" name="name" value="<?php echo isset($info['name']) ? $info['name'] : '' ?>">
							</div>
							<div class="form-group">
								<label class="control-label" for="sort">排序</label>
								<input type="text" class="form-control" id="sort" name="sort" value="<?php echo isset($info['sort']) ? $info['sort'] : '' ?>">
							</div>
							<div class="form-group">
								<label class="control-label" for="count">统计数</label>
								<input type="text" class="form-control" id="count" name="count" value="<?php echo isset($info['count']) ? $info['count'] : '' ?>">
							</div>
						</div>
					</div>
				</div>
			</div>
			
			<div class="col-md-6">
				<div class="panel panel-success">
					<div class="panel-heading">
						<h3 class="panel-title">关联分类</h3>
					</div>
					<div class="panel-body">
						
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