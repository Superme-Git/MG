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
                            <a href="/index"><i class="fa fa-dashboard fa-fw"></i> 面板</a>
                        </li>
                        <li>
                            <a href="/faq/index"><i class="fa fa-dashboard fa-fw"></i> FAQ首页</a>
                        </li>
                        <li>
                            <a href="/faq/category/"><i class="fa fa-dashboard fa-fw"></i> 分类管理</a>
                            <ul class="nav nav-second-level">
                                <li>
                                    <a href="/faq/category/">列表</a>
                                </li>
                                <li>
                                    <a href="/faq/category/add">添加</a>
                                </li>
                            </ul>
                        </li>
                        <li>
                            <a href="/faq/column/"><i class="fa fa-dashboard fa-fw"></i> 栏目管理</a>
                            <ul class="nav nav-second-level">
                                <li>
                                    <a href="/faq/column/">列表</a>
                                </li>
                                <li>
                                    <a href="/faq/column/add">添加</a>
                                </li>
                            </ul>
                        </li>
                        <li>
                            <a href="/faq/faq/"><i class="fa fa-dashboard fa-fw"></i> FAQ管理</a>
                            <ul class="nav nav-second-level">
                                <li>
                                    <a href="/faq/faq/">列表</a>
                                </li>
                                <li>
                                    <a href="/faq/faq/add">添加</a>
                                </li>
                                <li>
                                    <a href="/faq/faq/rebuild_indexer">重建索引</a>
                                </li>
                            </ul>
                        </li>
                        
                        <li>
                            <a href="/faq/record"><i class="fa fa-dashboard fa-fw"></i> 记录管理</a>
                            <ul class="nav nav-second-level">
                                <li>
                                    <a href="/faq/record">记录列表</a>
                                </li>
                                <li>
                                    <a href="/faq/record_subtable/index">记录分表管理</a>
                                </li>
                            </ul>
                        </li>
                        <!--
                        <li>
                            <a href="/mt3d_aq/setting"><i class="fa fa-dashboard fa-fw"></i> 活动设置</a>
                        </li>
                         
                        <li>
                            <a href="#"><i class="fa fa-bar-chart-o fa-fw"></i> Charts<span class="fa arrow"></span></a>
                            <ul class="nav nav-second-level">
                                <li>
                                    <a href="flot.html">Flot Charts</a>
                                </li>
                                <li>
                                    <a href="morris.html">Morris.js Charts</a>
                                </li>
                            </ul>
                        </li>
                        <li>
                            <a href="tables.html"><i class="fa fa-table fa-fw"></i> Tables</a>
                        </li>
                        <li>
                            <a href="forms.html"><i class="fa fa-edit fa-fw"></i> Forms</a>
                        </li>
                        <li>
                            <a href="#"><i class="fa fa-wrench fa-fw"></i> UI Elements<span class="fa arrow"></span></a>
                            <ul class="nav nav-second-level">
                                <li>
                                    <a href="panels-wells.html">Panels and Wells</a>
                                </li>
                                <li>
                                    <a href="buttons.html">Buttons</a>
                                </li>
                                <li>
                                    <a href="notifications.html">Notifications</a>
                                </li>
                                <li>
                                    <a href="typography.html">Typography</a>
                                </li>
                                <li>
                                    <a href="icons.html"> Icons</a>
                                </li>
                                <li>
                                    <a href="grid.html">Grid</a>
                                </li>
                            </ul>
                        </li>
                        <li>
                            <a href="#"><i class="fa fa-sitemap fa-fw"></i> Multi-Level Dropdown<span class="fa arrow"></span></a>
                            <ul class="nav nav-second-level">
                                <li>
                                    <a href="#">Second Level Item</a>
                                </li>
                                <li>
                                    <a href="#">Second Level Item</a>
                                </li>
                                <li>
                                    <a href="#">Third Level <span class="fa arrow"></span></a>
                                    <ul class="nav nav-third-level">
                                        <li>
                                            <a href="#">Third Level Item</a>
                                        </li>
                                        <li>
                                            <a href="#">Third Level Item</a>
                                        </li>
                                        <li>
                                            <a href="#">Third Level Item</a>
                                        </li>
                                        <li>
                                            <a href="#">Third Level Item</a>
                                        </li>
                                    </ul>
                                </li>
                            </ul>
                        </li>
                        <li>
                            <a href="#"><i class="fa fa-files-o fa-fw"></i> Sample Pages<span class="fa arrow"></span></a>
                            <ul class="nav nav-second-level">
                                <li>
                                    <a href="blank.html">Blank Page</a>
                                </li>
                                <li>
                                    <a href="login.html">Login Page</a>
                                </li>
                            </ul>
                        </li>
                         -->
                    </ul>
                </div>
                <!-- /.sidebar-collapse -->
            </div>
            <!-- /.navbar-static-side -->
        </nav>