using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Networking;

public class postMessageJSON : MonoBehaviour {
    // upload JSON to HTTP server //
    void Start()
    {
        StartCoroutine(PostRequest("http://192.168.0.23:8080/","Luca_Zeni", "Test1234", "Hello My Name Is Luca Zeni And I Attend York Univeristy"));  // example on how to use method
    }

    IEnumerator PostRequest(string url, string id, string fileName, string json)
    {
        if(id.Contains(" "))
        {
            throw new Exception("No spaces allowed in id name");
        }
        if (fileName.Contains(" "))
        {
            throw new Exception("No spaces allowed in file name");
        }
        else if(fileName.Equals("") || fileName == null)
        {
            throw new Exception("File name null");
        }

        var request = new UnityWebRequest(url, "POST"); // type of HTTPREQUEST POST in this case
        byte[] infoToSend = new System.Text.UTF8Encoding().GetBytes(id + " " + fileName + " " + json); 

        request.uploadHandler = (UploadHandler)new UploadHandlerRaw(infoToSend); 
        
        request.SetRequestHeader("Content-Type", "application/json"); 

        //Send the request then wait here until it returns
        yield return request.SendWebRequest();

        if (request.isNetworkError)
        {
            Debug.Log("Error While Sending: " + request.error);
        }
        else
        {
            Debug.Log("Received");
        }
    }

}
