<?php include __DIR__."/../_header.php";?>
<?php include __DIR__."/../_menu.php";?>

    <div id="page-wrapper">
		<div class="row">
			<div class="col-md-12">
				<h1 class="page-header">编辑用户</h1>
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
						<h3 class="panel-title">编辑用户资料</h3>
					</div>
					<div class="panel-body">
						<div class="col-md-9">
							<div class="form-group ">
								<label class="control-label" for="email">Email</label>
								<input type="text" class="form-control" id="email" name="email" value="<?php echo $info['email']?>">
							</div>
							<div class="form-group ">
								<label class="control-label" for="password">密码</label>
								<input type="text" class="form-control" id="password" name="password" value="<?php echo $info['password']?>">
							</div>
							<div class="form-group ">
								<label class="control-label" for="role">角色</label>
								<select name="role" class="form-control">
									<?php foreach (get_roles() as $k => $v){?>
									<option value="<?php echo $k?>" <?php echo !empty($info['role']) && $info['role'] == $k ? 'selected' : ''?>><?php echo $v?></option>
                                    <?php }?>
								</select>
							</div>
						</div>
					</div>
				</div>
			</div>
			
			<div class="col-md-6">
				<div class="panel panel-success">
					<div class="panel-heading">
						<h3 class="panel-title"></h3>
					</div>
					<div class="panel-body">
						<div class="col-md-9">
						</div>
					</div>
				</div>
			</div>
			
			<div class="col-md-12">
				<div class="panel panel-default">
					<div class="panel-body">
						<div class="col-md-9">
							<div class="form-group">
								<button type="submit" class="btn btn-default">提交表单</button>
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