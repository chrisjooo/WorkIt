using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class CameraRotation : MonoBehaviour
{
    float mPrevPos = 0;
    float mPosDelta = 0;
    public float speed;

    private void Update()
    {
        if (Input.GetMouseButton(0))
        {
            mPosDelta = Input.mousePosition.x - mPrevPos;
            transform.Rotate(0, mPosDelta * speed * Time.deltaTime, 0);
        }

        mPrevPos = Input.mousePosition.x;
    }
}
