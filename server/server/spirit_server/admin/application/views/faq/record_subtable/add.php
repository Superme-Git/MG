<?php include APPPATH . "views/_header.php";?>
<?php include APPPATH . "views/faq/_menu.php";?>

    <div id="page-wrapper">
		<div class="row">
			<div class="col-md-12">
				<h1 class="page-header">创建水平分表</h1>
			</div>
			<!-- /.col-md-12 -->
		</div>
		<?php include APPPATH ."views/_alert.php";?>
		<!-- /.row -->
        <div class="row">
        	<form role="form" method="post" action="" class="form-horizontal" enctype="multipart/form-data">
        	<div class="col-md-6">
				<div class="panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title">填写表单</h3>
					</div>
					<div class="panel-body">
						<div class="col-md-10">
							<div class="form-group ">
								<label class="control-label" for="key">KEY</label>
								<select name="key" class="form-control">
									<?php if ($recordList){foreach ($recordList as $item){?>
									<option value="<?php echo $item['month']?>"><?php echo $item['month']?></option>
									<?php }}?>
								</select>
							</div>
							<div class="form-group ">
								<label class="control-label" for="name">名称</label>
								<input type="text" class="form-control" id="name" name="name" value="">
							</div>
							<div class="form-group ">
								<label class="control-label" for="sort">排序</label>
								<input type="text" class="form-control" id="sort" name="sort" value="0">
							</div>
						</div>
					</div>
				</div>
			</div>
			
			<div class="col-md-6">
				<div class="panel panel-success">
					<div class="panel-heading">
						<h3 class="panel-title">待定</h3>
					</div>
					<div class="panel-body">
						<div class="col-md-10">
						</div>
					</div>
				</div>
			</div>
			
			<div class="col-md-12">
				<div class="panel panel-default">
					<div class="panel-body">
						<div class="col-md-10">
							<div class="form-group">
								<button type="submit" class="btn btn-primary">提交</button>
							</div>
						</div>
					</div>
				</div>
			</div>
			</form>
			
        </div>
    </div>

<?php include APPPATH . "views/_footer.php";?>