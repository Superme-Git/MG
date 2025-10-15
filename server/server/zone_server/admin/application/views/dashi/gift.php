<?php include APPPATH . "views/_header.php";?>
<?php include APPPATH . "views/_menu_dashi.php";?>

    <div id="page-wrapper">
		<div class="row">
			<div class="col-md-12">
				<h1 class="page-header">增加礼物数</h1>
			</div>
			<!-- /.col-md-12 -->
		</div>
		<?php include APPPATH ."views/_alert.php";?>
		
	   <div class="row">
		  <div class="col-md-12">
        </div>
		
		
		<!-- /.row -->
        <div class="row">
        	<form role="form" method="post" action="" class="form-horizontal">
        	<div class="col-md-12">
        	   <div class="panel panel-default">
                	<div class="panel-heading">服务器列表&nbsp;&nbsp;&nbsp;&nbsp;
                	   <input type="button" onclick="check_all('server_ids[]')" value="全选" />
                	   <input type="button" onclick="uncheck_all('server_ids[]')" value="全不选" />
                	</div>
                	<div class="panel-body" >
                	   <?php if ($server_list){foreach ($server_list as $item){if($item['amount'] < 0){continue;}?>
                	   
                	   <input type="checkbox" name="server_ids[]" value="<?php echo $item['serverkey']?>" id="serverlist<?php echo $item['serverkey']?>">
                	   <label for="serverlist<?php echo $item['serverkey']?>" ><?php echo $item['servername']?>(<?php echo $item['amount']?>)</label>
                	   
                	   <?php }}?>
                	</div>
                </div>
                
                <div class="panel panel-default">
                	<div class="panel-heading">不可用服务器列表</div>
                	<div class="panel-body" >
                	   <?php if ($server_list){foreach ($server_list as $item){if($item['amount'] > 0){continue;}?>
                	   
                	   <input type="checkbox" name="server_ids[]" value="<?php echo $item['serverkey']?>" id="serverlist<?php echo $item['serverkey']?>">
                	   <label for="serverlist<?php echo $item['serverkey']?>" ><?php echo $item['servername']?>(<?php echo $item['amount']?>)</label>
                	   
                	   <?php }}?>
                	</div>
                </div>
            
				<div class="panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title">增加礼物数</h3>
					</div>
					<div class="panel-body">
						<div class="col-md-6">
							<div class="form-group">
								<label class="control-label" for="gift_count">礼物数</label>
								<input class="form-control" id="gift_count" name="gift_count" value="0"/>
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
								<button type="submit" class="btn btn-primary">提交表单</button>
							</div>
						</div>
					</div>
				</div>
			</div>
			</form>
        </div>
    </div>
    
    <script language="javascript" type="text/javascript" src="/resource/js/My97DatePicker/WdatePicker.js"></script>
<?php include APPPATH . "views/_footer.php";?>