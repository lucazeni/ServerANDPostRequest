using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Networking;

public class postMessageJSON : MonoBehaviour {
    // upload JSON to HTTP server //
    
    void Start()
    {
        StartCoroutine(PostRequest("http://192.168.0.20:8080/","id", "LucaZ", "Sample Text 12345 Hello World"));  
    }

    IEnumerator PostRequest(string url, string id, string fileName, string json)
    {
        String encodedID = Uri.EscapeUriString(id); // encodes any non valid characters such as spaces, dashes etc
        String encodedFileName = Uri.EscapeUriString(fileName); // encodes any non valid characters such as spaces, dashes etc

        var request = new UnityWebRequest(url, "POST"); // type of HTTPREQUEST POST in this case
        byte[] infoToSend = new System.Text.UTF8Encoding().GetBytes(encodedID + " " + encodedFileName + " " + json); //payload

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
