package com.example.spacejet;

public class JoyStick {
    float ctrlZoneXpos;
    float ctrlZoneYpos;
    float ctrlRadius;
    float stickXpos;
    float stickYpos;
    float stickRadius;

    JoyStick() {
        ctrlRadius = 150;
        stickRadius = 75;
        ctrlZoneYpos = -1;
        ctrlZoneXpos = -1;
        stickXpos = ctrlZoneXpos;
        stickYpos = ctrlZoneYpos;
    }
}
