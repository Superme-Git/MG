<?php include APPPATH . "views/_header.php";?>
<?php include APPPATH . "views/faq/_menu.php";?>

    <div id="page-wrapper">
		<div class="row">
			<div class="col-md-12">
				<h1 class="page-header">添加FAQ</h1>
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
								<label class="control-label" for="title">标题</label>
								<input type="text" class="form-control" id="title" name="title" value="<?php echo isset($olddata['title']) ? $olddata['title'] : '' ?>">
							</div>
							<div class="form-group">
								<label class="control-label" for="style">样式</label>
								<input type="text" class="form-control" id="style" name="style" value="<?php echo isset($olddata['style']) ? $olddata['style'] : '' ?>">
							</div>
							<div class="form-group">
								<label class="control-label" for="category_id">分类</label>
								<select class="form-control" id="category_id" name="category_id">
                                    <option value="0">未选择</option>
                                    <?php if ($categoryList){foreach ($categoryList as $item){?>
                                    <option value="<?php echo $item['category_id']?>" <?php echo isset($olddata['category_id']) && $olddata['category_id'] == $item['category_id'] ? 'selected' : '' ?>>
                                    	<?php echo $item['name']?>
                                    		<?php echo !empty($item['level_low']) ? '('.$item['level_low'] : ''?>
                                    			<?php echo !empty($item['level_hight']) ? $item['level_hight'] . ')' : ''?></option>
                                    <?php }}?>
                                </select>
							</div>
							<div class="form-group">
								<label class="control-label" for="keywords">关键词</label>
								<input type="text" class="form-control" id="keywords" name="keywords" value="<?php echo isset($olddata['keywords']) ? $olddata['keywords'] : '' ?>">
								<p class="help-block">多个关键词用空格或逗号隔开.</p>
							</div>
							<div class="form-group">
								<label class="control-label" for="sort">排序</label>
								<input type="text" class="form-control" id="sort" name="sort" value="<?php echo isset($olddata['sort']) ? $olddata['sort'] : '' ?>">
							</div>
							<div class="form-group">
								<label class="control-label" for="sort">内容</label>
								<textarea name="content" style="width:100%;height:400px;"><?php echo isset($olddata['content']) ? $olddata['content'] : ''?></textarea>
								
							</div>
							
							
						</div>
					</div>
				</div>
			</div>
			
			<div class="col-md-6">
				<div class="panel panel-success">
					<div class="panel-heading">
						<h3 class="panel-title">关联栏目</h3>
					</div>
					<div class="panel-body">
						<div class="col-md-12">
							<div class="form-group">
								排序&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;栏目选项
							</div>
							<?php if ($columnList){foreach ($columnList as $item){?>
							<div class="form-group">
								<input type="text" name="sorts[<?php echo $item['column_id']?>]" value="<?php echo isset($faqToColumnArr[$item['column_id']]) ? $faqToColumnArr[$item['column_id']]['sort'] : '0'?>" 
									<?php echo isset($faqToColumnArr[$item['column_id']]) ? 'checked' : ''?> size="5">
								<input type="checkbox" id="column_ids_<?php echo $item['column_id']?>" name="column_ids[<?php echo $item['column_id']?>]" value="<?php echo $item['column_id']?>" <?php echo isset($faqToColumnArr[$item['column_id']]) ? 'checked' : ''?>>
									<label for="column_ids_<?php echo $item['column_id']?>"><?php echo $item['name']?></label>
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