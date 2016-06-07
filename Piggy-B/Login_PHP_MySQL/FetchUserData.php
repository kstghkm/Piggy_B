<?php
	$con = mysqli_connect("mysql9.000webhost.com" , "a1150590_root", "123456a", "a1150590_root");
	
	$username = $_POST["username"];
	$password = $_POST["password"];
	
	$selectquery = mysqli_prepare($con , "SELECT * FROM contacts WHERE username = ? AND Password = ?");
	mysqli_stmt_bind_param($selectquery , "ss" , $username , $password);
	mysqli_stmt_execute($selectquery);
	
	mysqli_stmt_store_result($selectquery);
	mysqli_stmt_bind_result($selectquery ,  $ID , $name , $phone , $username , $password);
	
	$user = array();
	
	while(mysqli_stmt_fetch($selectquery))
	{
		$user[name] = $name;
		$user[phone] = $phone;
		$user[username] = $username;
		$user[password] = $password;
	}
	
	echo json_encode($user);
	
	mysqli_stmt_close($selectquery);
		
	mysqli_close($con);
?>