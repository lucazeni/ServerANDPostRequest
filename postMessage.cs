using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Networking;
using UnityEngine.UI;
public class postMessage : MonoBehaviour
{
    // upload String to HTTP server
    void Start()
    {
        StartCoroutine(PostRequest("http://localhost:8080/"));
    }

    IEnumerator PostRequest(string url)
    {
        WWWForm form = new WWWForm();
        form.AddField("myName", "Luca Zeni");
        form.AddField("projectName", "Occutherapy");

        UnityWebRequest uwr = UnityWebRequest.Post(url, form);
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
