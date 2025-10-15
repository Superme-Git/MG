


<?php 
if($errordata = $this->session->flashdata('error'))
{
    $message = '';
    if (is_array($errordata))
    {
        foreach ($errordata as $key => $item)
        {
            $message .= $item."<br />";
        }
    }
    else
    {
        $message .= $errordata;
    }
    
    ?>
    <div class="alert alert-danger"><?php echo $message?></div>
    <?php 
}
elseif ($successdata = $this->session->flashdata('success'))
{
    $message = '';
    if (is_array($successdata))
    {
        foreach ($successdata as $key => $item)
        {
            $message .= $item."<br />";
        }
    }
    else
    {
        $message .= $successdata;
    }
    
    ?>
    <div class="alert alert-success"><?php echo $message?></div>
    <?php 
}
else if (!empty($message)){
?>
<div class="alert alert-warning"><?php echo $message?></div>
<?php }?>