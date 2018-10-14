using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Networking;

public class getMessage : MonoBehaviour {
    //get data from HTTP server
    void Start()
    {
        StartCoroutine(GetRequest("http:///www.yoururl.com"));
    }

    IEnumerator GetRequest(string uri)
    {
        UnityWebRequest uwr = UnityWebRequest.Get(uri);
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
