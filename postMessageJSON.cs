using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Networking;

public class postMessageJSON : MonoBehaviour {
    // upload JSON to HTTP server
    void Start()
    {
        StartCoroutine(PostRequest("http:///www.yoururl.com", "your json")); 
    }

    IEnumerator PostRequest(string url, string json) // URL + JSON
    {
        var uwr = new UnityWebRequest(url, "POST"); // type of HTTPREQUEST
        byte[] jsonToSend = new System.Text.UTF8Encoding().GetBytes(json); // encode JSON
        uwr.uploadHandler = (UploadHandler)new UploadHandlerRaw(jsonToSend); // upload JSON
        uwr.downloadHandler = (DownloadHandler)new DownloadHandlerBuffer(); 
        uwr.SetRequestHeader("Content-Type", "application/json"); // set type

        //Send the request then wait here until it returns
        yield return uwr.SendWebRequest();

        if (uwr.isNetworkError)
        {
            Debug.Log("Error While Sending: " + uwr.error);
        }
        else
        {
            Debug.Log("Received: " + uwr.downloadHandler.text);
        }
    }

}
