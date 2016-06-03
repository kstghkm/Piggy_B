<?php
	$con = mysqli_connect("localhost" , "my_user", "my_password", "my_db");
	
	$username = $_POST["username"];
	$password = $_POST["password"];
	
	$selectquery = mysqli_prepare($con , "SELECT * FROM contacts WHERE Username = ? AND Password = ?");
	mysqli_stmt_param($selectquery , "ss" , $username , $password);
	mysqli_stmt_execute($selectquery);
	
	mysqli_stmt_store_result($selectquery);
	mysqli_stmt_bind_result($selectquery ,  $ID , $name , $email , $username , $password);
	
	$user = array();
	
	while(mysqli_stmt_fetch($selectquery))
	{
		$user[name] = $name;
		$user[email] = $email;
		$user[username] = $username;
		$user[password] = $password;
	}
	
	echo json_encode($user);
	
	mysqli_stmt_close($selectquery);
		
	mysqli_close($con);
?>