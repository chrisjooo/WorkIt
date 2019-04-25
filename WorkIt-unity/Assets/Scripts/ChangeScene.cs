using System.Collections;
using System.Collections.Generic;
using UnityEngine;


public class ChangeScene : MonoBehaviour
{
    public void changeToScene (int changeTheScene) {
        UnityEngine.SceneManagement.SceneManager.LoadScene(changeTheScene);
    }
}
