		<!-- Navigation -->
        <nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0">

            <?php include APPPATH . "views/_navbar.php";?>
            
            <div class="navbar-default sidebar" role="navigation">
                <div class="sidebar-nav navbar-collapse">
                    <ul class="nav" id="side-menu">
                        <li class="sidebar-search">
                            <div class="input-group custom-search-form">
                                <input type="text" class="form-control" placeholder="Search...">
                                <span class="input-group-btn">
                                <button class="btn btn-default" type="button">
                                    <i class="fa fa-search"></i>
                                </button>
                            </span>
                            </div>
                            <!-- /input-group -->
                        </li>
                        <li>
                            <a href="/serverlist/index"><i class="fa fa-dashboard fa-fw"></i> 面板</a>
                        </li>
                        <li>
                            <a href="/admin/index"><i class="fa fa-dashboard fa-fw"></i> 管理员</a>
                            <ul class="nav nav-second-level">
                            	<li>
                            		<a href="/admin/index">列表</a>
                                </li>
                                <li>
                                    <a href="/admin/add">添加</a>
                                </li>
                                
                            </ul>
                        </li>
                        <li class="list-group-item disabled"></li>
                        <li>
                            <a href="/role/index"><i class="fa fa-dashboard fa-fw"></i> 头像列表</a>
                        </li>
                    </ul>
                </div>
                <!-- /.sidebar-collapse -->
            </div>
            <!-- /.navbar-static-side -->
            
        </nav>