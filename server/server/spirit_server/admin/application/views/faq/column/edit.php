<?php include APPPATH . "views/_header.php";?>
<?php include APPPATH . "views/faq/_menu.php";?>

    <div id="page-wrapper">
		<div class="row">
			<div class="col-md-12">
				<h1 class="page-header">编辑栏目</h1>
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
							
						</div>
					</div>
				</div>
			</div>
			
			<div class="col-md-6">
				<div class="panel panel-success">
					<div class="panel-heading">
						<h3 class="panel-title">关联FAQ帖子</h3>
					</div>
					<div class="panel-body">
						<div class="col-md-12">
							<div class="form-group">
								<label class="control-label" for="name">FAQ帖子列表</label>
							</div>
							<?php if ($faqList){foreach ($faqList as $k => $item1){?>
							<div class="row">
							<?php if ($item1['info']){?>
							<div class="form-group" >
								<i class="glyphicon <?php echo $k ? 'glyphicon-plus' : 'glyphicon-minus'?>" onclick="$('#faq_list<?php echo $k?>').toggle()"></i>
								<label class="control-label" onclick="$('#faq_list<?php echo $k?>').toggle();"><?php echo $item1['info']['name']?></label>
							</div>
							<?php }?>
							<div class="form-group" id="faq_list<?php echo $k?>" style="<?php echo $k ? 'display:none' : ''?>">
							<?php if ($item1['list']){foreach ($item1['list'] as $item){?>
								<div class="col-md-6">
								  	<input type="text" for="faq_ids_<?php echo $item['faq_id']?>" name="sorts[<?php echo $item['faq_id']?>]" value="<?php echo isset($faqToColumnArr[$item['faq_id']]) ? $faqToColumnArr[$item['faq_id']]['sort'] : '0'?>" 
										<?php echo isset($faqToColumnArr[$item['faq_id']]) ? 'checked' : ''?> size="5">
								    <input type="checkbox" id="faq_ids_<?php echo $item['faq_id']?>" name="faq_ids[<?php echo $item['faq_id']?>]" value="1" <?php echo isset($faqToColumnArr[$item['faq_id']]) ? 'checked' : '' ?>>
								    <?php echo $item['title']?>
								</div>
							<?php }}?>
							</div>
							</div>
							<?php }}?>
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