using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Menu : MonoBehaviour
{
    public Camera[] cams;

    public void camPushUp()
    {
        cams[0].enabled = false;
        cams[1].enabled = true;
        cams[2].enabled = false;
        cams[3].enabled = false;
    }

    public void camSitUp()
    {
        cams[0].enabled = false;
        cams[1].enabled = false;
        cams[2].enabled = true;
        cams[3].enabled = false;
    }

    public void camJogging()
    {
        cams[0].enabled = false;
        cams[1].enabled = false;
        cams[2].enabled = false;
        cams[3].enabled = true;
    }
}
