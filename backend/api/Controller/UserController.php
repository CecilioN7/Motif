<?php
require_once("BaseController.php");
class UserController extends BaseController
{
	public function listAction()
	{
		$strErrorDesc = '';
		$request = $_SERVER["REQUEST_METHOD"];
		$arrQueryStringParams = $this->getQueryStringParams();
		if (strtoupper($request) == 'GET'){
			try{
				$userModel = new UserModel();
				$intLimit = 10;
				if (isset($arrQueryStringParams['limit']) && $arrQueryStringParams['limit']){
					$intLimit = $arrQueryStringParams['limit'];
				}
				$arrUsers = $userModel->getAllUsers($intLimit);
				$responseData = json_encode($arrUsers);	
			} catch (Error $e) {
				$strErrorDesc = $e->getMessage().' Something went wrong!';
				$strErrorHeader = 'HTTP/1.1 500 Internal Server Error';
			}
		} else {
			$strErrorDesc = 'Method not supported';
			$strErrorHeader = 'HTTP/1.1 422 Unprocessable Entity';
		}

		if (!$strErrorDesc){
			$this->sendOutput(
				$responseData,
				array('Content-Type: application/json', 'HTTP/1.1 200 OK')
			);
		} else {
			$this->sendOutput(json_encode(array('error' => $strErrorDesc)),
				array('Content-Type: application/json', $strErrorHeader)
			);
		}
	}
	public function changePasswordAction()
	{
		$strErrorDesc = '';
		$request = $_SERVER["REQUEST_METHOD"];
		$arrQueryStringParams = $this->getQueryStringParams();
		if (strtoupper($request) == 'GET'){
			try{
				$userModel = new UserModel();
				if (isset($arrQueryStringParams['username']) && $arrQueryStringParams['username']){
					$username = $arrQueryStringParams['username'];
				}
				if (isset($arrQueryStringParams['password']) && $arrQueryStringParams['password']){
					$password = $arrQueryStringParams['password'];
				}
				$success = $userModel->changePassword($password, $username);
				if ($success){
					$responseData = json_encode(array("Result"=>true));	
				} else {
					$responseData = json_encode(array("Result"=>false));
				}
			} catch (Error $e) {
				$strErrorDesc = $e->getMessage().' Something went wrong!';
				$strErrorHeader = 'HTTP/1.1 500 Internal Server Error';
			}
		} else {
			$strErrorDesc = 'Method not supported';
			$strErrorHeader = 'HTTP/1.1 422 Unprocessable Entity';
		}

		if (!$strErrorDesc){
			$this->sendOutput(
				$responseData,
				array('Content-Type: application/json', 'HTTP/1.1 200 OK')
			);
		} else {
			$this->sendOutput(json_encode(array('error' => $strErrorDesc)),
				array('Content-Type: application/json', $strErrorHeader)
			);
		}
	}
	public function idAction()
	{
		$strErrorDesc = '';
		$request = $_SERVER["REQUEST_METHOD"];
		$arrQueryStringParams = $this->getQueryStringParams();
		if (strtoupper($request) == 'GET'){
			try{
				$userModel = new UserModel();
				if (isset($arrQueryStringParams['user'])){
					$user = $arrQueryStringParams['user'];
				}
				$arrID = $userModel->getID($user);
				$responseData = json_encode($arrID);	
			} catch (Error $e) {
				$strErrorDesc = $e->getMessage().' Something went wrong!';
				$strErrorHeader = 'HTTP/1.1 500 Internal Server Error';
			}
		} else {
			$strErrorDesc = 'Method not supported';
			$strErrorHeader = 'HTTP/1.1 422 Unprocessable Entity';
		}

		if (!$strErrorDesc){
			$this->sendOutput(
				$responseData,
				array('Content-Type: application/json', 'HTTP/1.1 200 OK')
			);
		} else {
			$this->sendOutput(json_encode(array('error' => $strErrorDesc)),
				array('Content-Type: application/json', $strErrorHeader)
			);
		}
	}

	public function searchAction()
	{
		$strErrorDesc = '';
		$request = $_SERVER["REQUEST_METHOD"];
		$arrQueryStringParams = $this->getQueryStringParams();
		if (strtoupper($request) == 'GET'){
			try{
				$userModel = new UserModel();
				//echo "Here!";

				$search = '';
				if (isset($arrQueryStringParams['user']) && $arrQueryStringParams['user']){
					$search = $arrQueryStringParams['user'];
					$arrUsers = $userModel->searchUser($search);
				}
				$responseData = json_encode($arrUsers);	
			} catch (Error $e) {
				$strErrorDesc = $e->getMessage().'Something went wrong!';
				$strErrorHeader = 'HTTP/1.1 500 Internal Server Error';
			}
		} else {
			$strErrorDesc = 'Method not supported';
			$strErrorHeader = 'HTTP/1.1 422 Unprocessable Entity';
		}

		if (!$strErrorDesc){
			$this->sendOutput(
				$responseData,
				array('Content-Type: application/json', 'HTTP/1.1 200 OK')
			);
		} else {
			$this->sendOutput(json_encode(array('error' => $strErrorDesc)),
				array('Content-Type: application/json', $strErrorHeader)
			);
		}
	}
	public function getAction()
	{
		//echo "Wow!";	
		$strErrorDesc = '';
		$request = $_SERVER["REQUEST_METHOD"];
		$arrQueryStringParams = $this->getQueryStringParams();
		#print_r($arrQueryStringParams);
		if (strtoupper($request) == 'GET'){
			try{
				$userModel = new UserModel();
				//echo "Here!";

				$ID = null;
				if (isset($arrQueryStringParams['id']) && $arrQueryStringParams['id']){
					$ID = $arrQueryStringParams['id'];
				}
				settype($ID, "integer");	
				if (is_int($ID)){
					$arrUsers = $userModel->getUser($ID);
					$responseData = json_encode($arrUsers);	
				}
				//echo $ID;
			} catch (Error $e) {
				$strErrorDesc = $e->getMessage().'Something went wrong!';
				$strErrorHeader = 'HTTP/1.1 500 Internal Server Error';
			}
		} else {
			$strErrorDesc = 'Method not supported';
			$strErrorHeader = 'HTTP/1.1 422 Unprocessable Entity';
		}

		if (!$strErrorDesc){
			$this->sendOutput(
				$responseData,
				array('Content-Type: application/json', 'HTTP/1.1 200 OK')
			);
		} else {
			$this->sendOutput(json_encode(array('error' => $strErrorDesc)),
				array('Content-Type: application/json', $strErrorHeader)
			);
		}
	}
	
	public function addAction()
	{
		//echo "Wow!";	
		$strErrorDesc = '';
		$request = $_SERVER["REQUEST_METHOD"];
		$arrQueryStringParams = $this->getQueryStringParams();
		if (strtoupper($request) == 'GET'){
			try{
				$userModel = new UserModel();
				//echo "Here!";

				$username = null;
				$password = null;
				$email = null;

				if (isset($arrQueryStringParams['user']) && $arrQueryStringParams['user']){
					$username = $arrQueryStringParams['user'];
				}
				if (isset($arrQueryStringParams['pass']) && $arrQueryStringParams['pass']){
					$password = $arrQueryStringParams['pass'];
				}
				if (isset($arrQueryStringParams['email']) && $arrQueryStringParams['email']){
					$email = $arrQueryStringParams['email'];
				}
				if (isset($arrQueryStringParams['name']) && $arrQueryStringParams['name']){
					$name = $arrQueryStringParams['name'];
				}
				if (isset($arrQueryStringParams['phone']) && $arrQueryStringParams['phone']){
					$phone = $arrQueryStringParams['phone'];
				}
				//echo $username;
				if ($username && $password && $email && $name && $phone){
					$success = $userModel->addUser($username, $password, $email, $name, $phone);
					if ($success){
						$responseData = json_encode(array("Result"=>true));	
					}
				}
			} catch (Error $e) {
				$strErrorDesc = $e->getMessage().'Something went wrong!';
				$strErrorHeader = 'HTTP/1.1 500 Internal Server Error';
			}
		} else {
			$strErrorDesc = 'Method not supported';
			$strErrorHeader = 'HTTP/1.1 422 Unprocessable Entity';
		}

		if (!$strErrorDesc){
			$this->sendOutput(
				$responseData,
				array('Content-Type: application/json', 'HTTP/1.1 200 OK')
			);
		} else {
			$this->sendOutput(json_encode(array('error' => $strErrorDesc)),
				array('Content-Type: application/json', $strErrorHeader)
			);
		}
	
	}	
}

