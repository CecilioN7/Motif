<?php
require_once("BaseController.php");
class NotesheetsController extends BaseController
{
	public function listAction()
	{
		$strErrorDesc = '';
		$request = $_SERVER["REQUEST_METHOD"];
		$arrQueryStringParams = $this->getQueryStringParams();
		if (strtoupper($request) == 'GET'){
			try{
				$notesheetsModel = new NotesheetsModel();
				$userModel = new UserModel();
				if (isset($arrQueryStringParams['user'])){
					$user = $arrQueryStringParams['user'];
					$arrID = $userModel->getID($user);
					$ID = $arrID['0']['ID'];
				}
				$notesheets = $notesheetsModel->getUserSheets($ID);
				$responseData = json_encode($notesheets);	
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
	public function updateAction()
	{
		$strErrorDesc = '';
		$request = $_SERVER["REQUEST_METHOD"];
		$arrQueryStringParams = $this->getQueryStringParams();
		if (strtoupper($request) == 'GET'){
			try{
				$notesheetsModel = new NotesheetsModel();
				if (isset($arrQueryStringParams['ID']) && isset($arrQueryStringParams['notes'])){
					$ID = $arrQueryStringParams['ID'];
					$notes = $arrQueryStringParams['notes'];
				}
				$notesheets = $notesheetsModel->updateSheet($ID, $notes);
				$responseData = json_encode($notesheets);	
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
	public function deleteAction()
	{
		$strErrorDesc = '';
		$request = $_SERVER["REQUEST_METHOD"];
		$arrQueryStringParams = $this->getQueryStringParams();
		if (strtoupper($request) == 'GET'){
			try{
				$notesheetsModel = new NotesheetsModel();
				$userModel = new UserModel();
				if (isset($arrQueryStringParams['ID'])){
					$ID = $arrQueryStringParams['ID'];
				}
				$notesheets = $notesheetsModel->removeSheet($ID);
				
				$responseData = json_encode($notesheets);	
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
	public function sheetAction()
	{
		$strErrorDesc = '';
		$request = $_SERVER["REQUEST_METHOD"];
		$arrQueryStringParams = $this->getQueryStringParams();
		if (strtoupper($request) == 'GET'){
			try{
				$notesheetsModel = new notesheetsModel();
				//echo "Here!";
				$ID = 0;
				if (isset($arrQueryStringParams['ID'])){
					$ID = $arrQueryStringParams['ID'];
					$arrSheet = $notesheetsModel->getSheet($ID);
				}
				$responseData = json_encode($arrSheet);	
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
		$strErrorDesc = '';
		$request = $_SERVER["REQUEST_METHOD"];
		$arrQueryStringParams = $this->getQueryStringParams();
		if (strtoupper($request) == 'GET'){
			try{
				$userModel = new UserModel();
				$notesheetsModel = new NotesheetsModel();
				$username = null;

				if (isset($arrQueryStringParams['user']) && $arrQueryStringParams['user']){
					$user = $arrQueryStringParams['user'];
					$arrID = $userModel->getID($user);
					$UID = $arrID['0']['ID'];
					$UID = (int)$UID;

				}
				if (isset($arrQueryStringParams['notes']) && $arrQueryStringParams['notes']){
					$notes = $arrQueryStringParams['notes'];
				}
				
				//echo $username;
				if ($notes && $UID){
					$success = $notesheetsModel->addSheet($UID, $notes);
					if ($success){
						$responseData = json_encode(array("Result"=>true));	
					} else {
						$responseData = json_encode(array("Result"=>false));
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

