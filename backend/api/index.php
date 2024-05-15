<?php
require __DIR__ . "/inc/bootstrap.php";
ini_set('display_error', 1);
error_reporting(E_ALL);
//echo "HELLO!";
if (!isset($_SERVER['REQUEST_URI'])){
	$_SERVER['REQUEST_URI'] = '/api/index.php/user/list';
	$_SERVER["REQUEST_METHOD"] = 'GET';
	$_SERVER['QUERY_STRING'] = "search=dumbbell";
}

$uri = parse_url($_SERVER['REQUEST_URI'], PHP_URL_PATH);
$uri = explode('/', $uri);
#print_r($uri);
//echo $_SERVER['QUERY_STRING'];
//parse_str($_SERVER['QUERY_STRING'], $query);
//print_r($query);

if ((isset($uri[4]) && $uri[4] != 'user' && $uri[4] != 'notesheets') || !isset($uri[5])){
	header("HTTP/1.1 404 Not Found");
	exit();
}

require PROJECT_ROOT_PATH . "/Controller/UserController.php";
require PROJECT_ROOT_PATH . "/Controller/NotesheetsController.php";

$model = ucfirst($uri[4]);

//echo "obj{$model}Controller";
$objUserController = new UserController();
$objNotesheetsController = new NotesheetsController();


$strMethodName = $uri[5] . 'Action';
try{
	//echo "$strMethodName()";
	//$objWorkoutController->{$strMethodName}();
	switch($model){
		case 'User':
			$objUserController->{$strMethodName}();
		case 'Notesheets':
			$objNotesheetsController->{$strMethodName}();
	}	
} catch(Exception $e){
	//echo "Tried to query but failed :(";
	
	echo 'Error: ' . $e->getMessage();
}
 
?>
