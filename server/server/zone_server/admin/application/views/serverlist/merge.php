<?php include APPPATH . "views/_header.php";?>
<?php include APPPATH . "views/_menu.php";?>


        <div id="page-wrapper">
            <div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header">合服</h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <?php include APPPATH ."views/_alert.php";?>
            
            <!-- /.row -->
            <div class="row">
                <form role="form" method="post" action="" class="form-horizontal">
                <div class="col-lg-12">
                
                    <div class="panel panel-default">
    					<div class="panel-heading"></div>
    					<div class="panel-body">
    						<a href="/serverlist/add" class="btn">添加服务器</a>
    						
    						<a href="/serverlist/merge" class="btn">合服</a>
    					</div>
    				</div>
                
                    <div class="panel panel-default">
                    	<div class="panel-heading"></div>
                    	<div class="panel-body">
                    		<div class="col-md-8">
                    		  <div class="form-group">
    								<label class="control-label" for="from_server">要合并的服务器</label>
    								<br />
    								<?php if ($server_list){foreach ($server_list as $item){?>
                	   
                            	   <input type="checkbox" name="from_servers[]" value="<?php echo $item['serverlist_id']?>" id="from_servers<?php echo $item['serverkey']?>">
                            	   <label for="from_servers<?php echo $item['serverkey']?>" ><?php echo $item['servername']?></label>
                            	   
                            	   <?php }}?>
    								
    								
    							</div>
    						</div>
    					   <div class="col-md-6">
    							<div class="form-group">
    								<label class="control-label" for="to_server">目标服务器</label>
    								<br />
    								<select name="to_server" class="form-control" >
    								<option value="0">请选择</option>
    								<?php if ($server_list){foreach ($server_list as $item){?>
    								<option value="<?php echo $item['serverlist_id']?>"><?php echo $item['servername']?></option>
    								<?php }}?>
    								</select>
    							</div>
    						</div>
						
                        </div>
                    </div>
                    <!-- /.panel -->
                    
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
            <!-- /.row -->
        </div>
        <!-- /#page-wrapper -->
    

    <?php include APPPATH . "views/_footer.php";?>
