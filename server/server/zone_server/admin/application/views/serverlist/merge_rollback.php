<?php include APPPATH . "views/_header.php";?>
<?php include APPPATH . "views/_menu.php";?>


        <div id="page-wrapper">
            <div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header">合服回滚</h1>
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
                    		<div class="col-md-8">
                    		  <div class="form-group">
    								<label class="control-label" for="from_server">要合并的服务器</label>
    								<br />
    								<?php echo implode(',', unserialize(base64_decode_($info['from_server_keys'])))?>
    							</div>
    						</div>
    					   <div class="col-md-6">
    							<div class="form-group">
    								<label class="control-label" for="to_server">目标服务器</label>
    								<br />
    								<?php echo $info['to_server_key']?>
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
    								<button type="submit" class="btn btn-primary">确定回滚</button>
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
