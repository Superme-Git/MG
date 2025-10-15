<?php include APPPATH . "views/_header.php";?>
<?php include APPPATH . "views/_menu.php";?>

    <div id="page-wrapper">
		<div class="row">
			<div class="col-md-12">
				<h1 class="page-header">编辑游戏</h1>
			</div>
			<!-- /.col-md-12 -->
		</div>
		<?php include APPPATH ."views/_alert.php";?>
		<!-- /.row -->
        <div class="row">
        	<form role="form" method="post" action="" class="form-horizontal">
        	<div class="col-md-6">
				<div class="panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title"></h3>
					</div>
					<div class="panel-body">
						<div class="col-md-8">
							<div class="form-group">
								<label class="control-label" for="gameid">游戏ID</label>
								<input type="text" class="form-control" id="gameid" name="gameid" value="<?php echo !empty($info['gameid']) ? $info['gameid'] : '' ?>">
							</div>
							<div class="form-group">
								<label class="control-label" for="name">游戏名</label>
								<input type="text" class="form-control" id="name" name="name" value="<?php echo !empty($info['name']) ? $info['name'] : '' ?>">
							</div>
						</div>
					</div>
				</div>
			</div>
			
			<div class="col-md-12">
				<div class="panel panel-default">
					<div class="panel-body">
						<div class="form-group">
							<div class="col-md-10">
								<button type="submit" class="btn btn-primary">提交表单</button>
							</div>
						</div>
					</div>
				</div>
			</div>
			</form>
        </div>
    </div>

<?php include APPPATH . "views/_footer.php";?>