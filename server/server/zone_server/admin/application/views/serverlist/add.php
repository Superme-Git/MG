<?php include APPPATH . "views/_header.php";?>
<?php include APPPATH . "views/_menu.php";?>

    <div id="page-wrapper">
		<div class="row">
			<div class="col-md-12">
				<h1 class="page-header">添加服务器</h1>
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
						<h3 class="panel-title">服务器信息</h3>
					</div>
					<div class="panel-body">
						<div class="col-md-8">
							<div class="form-group">
								<label class="control-label" for="serverkey">区服标识</label>
								<input type="text" class="form-control" id="serverkey" name="serverkey" value="<?php echo !empty($olddata['serverkey']) ? $olddata['serverkey'] : '' ?>">
							</div>
							<div class="form-group">
								<label class="control-label" for="servername">服务器名</label>
								<input type="text" class="form-control" id="servername" name="servername" value="<?php echo !empty($olddata['servername']) ? $olddata['servername'] : '' ?>">
							</div>
						</div>
					</div>
				</div>
			</div>
			
			<div class="col-md-6">
				<div class="panel panel-default">
					<div class="panel-body">
						<div class="col-md-8">
							<div class="form-group">
								<label class="control-label" for="hostname">数据库主机</label>
								<input type="text" class="form-control" id="hostname" name="hostname" value="<?php echo !empty($olddata['hostname']) ? $olddata['hostname'] : '' ?>">
							</div>
							<div class="form-group">
								<label class="control-label" for="weibo_port">数据库连接端口</label>
								<input type="text" class="form-control" id="port" name="port" value="<?php echo !empty($olddata['port']) ? $olddata['port'] : '' ?>">
							</div>
							<div class="form-group">
								<label class="control-label" for="username">数据库用户名</label>
								<input type="text" class="form-control" id="username" name="username" value="<?php echo !empty($olddata['username']) ? $olddata['username'] : '' ?>">
							</div>
							<div class="form-group">
								<label class="control-label" for="password">数据库密码</label>
								<input type="text" class="form-control" id="password" name="password" value="<?php echo !empty($olddata['password']) ? $olddata['password'] : '' ?>">
							</div>
							<div class="form-group">
								<label class="control-label" for="database">数据库名</label>
								<input type="text" class="form-control" id="database" name="database" value="<?php echo !empty($olddata['database']) ? $olddata['database'] : '' ?>">
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
								<button type="reset" class="btn btn-default">Reset Button</button>
							</div>
						</div>
					</div>
				</div>
			</div>
			</form>
        </div>
    </div>
<?php include APPPATH . "views/_footer.php";?>