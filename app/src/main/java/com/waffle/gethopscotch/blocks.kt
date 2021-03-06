package com.waffle.gethopscotch

import com.google.gson.Gson

val blocks: Map<String, ArrayList<String>> = Gson().fromJson<Map<String, ArrayList<String>>>(
    """
    {
    	19: ["old","Wait til Timestamp","milliseconds"],
    	20: ["old","\u2063"],
    	22: ["old","\u2063"],
    	23: ["move","Move Forward"," "],
    	24: ["move","Turn", "degrees"],
    	26: ["draw","Draw a Trail","color","width"],
    	27: ["move","Change X", "by"],
    	28: ["move","Change Y", "by"],
    	29: ["old","Scale", "by"],
    	30: ["draw","Clear"],
    	31: ["draw","Set Trail Width", "to"],
    	32: ["draw","Set Trail Color", "to"],
    	33: ["looks","Change Pose"],
    	34: ["move","Set Speed", "to"],
    	35: ["ctrl","Wait", "milliseconds"],
    	36: ["old","Set Opacity", "to"],
    	37: ["old","Pen Down"],
    	38: ["old","Pen Up"],
    	39: ["move","Set Angle"," "],
    	40: ["looks","Set Text","to","color"],
    	41: ["move","Set Position","to x","y"],
    	42: ["looks","Send To Back"],
    	43: ["looks","Bring To Front"],
    	44: ["var","Increase","by"],
    	45: ["var","Set","to"],
    	46: ["old","Move With Trail","distance"],
    	47: ["looks","Set Invisibility","percent"],
    	48: ["looks","Grow","by percent"],
    	49: ["looks","Shrink","by percent"],
    	50: ["move","Flip"],
    	51: ["looks","Set Size","percent"],
    	52: ["looks","Start Sound","wait"],
    	53: ["ctrl","Create a Clone of This Object","times"],
    	54: ["looks","Set Color"," "],
    	55: ["ctrl","Destroy"],
    	56: ["looks","Set Image"," "],
    	57: ["looks","Set","width","height"],
    	58: ["looks","Set Z Index"," "],
    	59: ["move","Set Origin","to x","y"],
    	60: ["move","Set Center","to x","y"],
    	61: ["ctrl","Wait","seconds"],
    	62: ["looks","Start Sound","wait"],
    	63: ["var","Save Input","prompt"],
    	64: ["looks","Set Text to Input","color"],
    	65: ["looks","Play Note","rhythm"],
    	66: ["looks","Set Tempo"," "],
    	67: ["looks","Set Instrument"," "],
    	68: ["ctrl","Open Project"," "],
    	120: ["ctrl","Repeat","times"],
    	121: ["ctrl","Repeat Forever"],
    	122: ["ctrl","Check Once If"," "],
    	123: ["abl","Ability"],
    	124: ["ctrl","Check If Else"," "],
    	125: ["ctrl","Change Scene","to"],
    	126: ["ctrl","Broadcast Message","named"],
    	127: ["ctrl","Request Seeds","for"],
    	233: ["Random"],
    	234: ["XPos"],
    	235: ["YPos"],
    	236: ["Random110"],
    	237: ["Random1100"],
    	238: ["Random11000"],
    	239: ["Variable"],
    	1000: ["conditional","="],
    	1001: ["conditional","\u2260"],
    	1002: ["conditional","???"],
    	1003: ["conditional","???"],
    	1004: ["conditional","and"],
    	1005: ["conditional","or"],
    	1006: ["conditional","???="],
    	1007: ["conditional","???="],
    	1008: ["conditional","matches"],
    	1009: ["HS_END_OF_CONDITIONAL_OPERATORS"],
    	2000: ["Rotation"],
    	2001: ["X Position"],
    	2002: ["Y Position"],
    	2003: ["Invisibility as a %"],
    	2004: ["Size as a %"],
    	2005: ["Speed"],
    	2006: ["Clone Index"],
    	2007: ["Total Clones"],
    	2008: ["Width"],
    	2009: ["Height"],
    	2010: ["Z Index"],
    	2011: ["Origin X"],
    	2012: ["Origin Y"],
    	2013: ["Center X"],
    	2014: ["Center Y"],
    	2015: ["Text"],
    	2016: ["Tempo"],
    	2017: ["Instrument"],
    	2018: ["HS_END_OF_OBJECT_TRAITS"],
    	2500: ["\uD83D\uDCF1 Username"],
    	2501: ["\uD83D\uDCF1 Time"],
    	2502: ["\uD83D\uDCF1 Year"],
    	2503: ["\uD83D\uDCF1 Month"],
    	2504: ["\uD83D\uDCF1 Day"],
    	2505: ["\uD83D\uDCF1 Hour"],
    	2506: ["\uD83D\uDCF1 Minute"],
    	2507: ["\uD83D\uDCF1 Second"],
    	2508: ["HS_END_OF_USER_TRAITS"],
    	3000: ["?????? Width"],
    	3001: ["?????? Height"],
    	3002: ["?????? Tilt Up %"],
    	3003: ["?????? Tilt Down %"],
    	3004: ["?????? Tilt Left %"],
    	3005: ["?????? Tilt Right %"],
    	3006: ["?????? Last Touch X"],
    	3007: ["?????? Last Touch Y"],
    	3008: ["?????? Total Objects"],
    	3009: ["HS_END_OF_STAGE_TRAITS"],
    	4000: ["math","+"],
    	4001: ["math","\u2212"],
    	4002: ["math","\u00D7"],
    	4003: ["math","\u00F7"],
    	4004: ["math","\u2063 random","to"],
    	4005: ["math","^"],
    	4006: ["math","\u2063 \u221A"," "],
    	4007: ["math","\u2063 sin"," "],
    	4008: ["math","\u2063 cos"," "],
    	4009: ["math","\u2063 round"," "],
    	4010: ["math","\u2063 absolute value"," "],
    	4011: ["math","%"],
    	4012: ["math","\u2063 tan"," "],
    	4013: ["math","\u2063 arcsin"," "],
    	4014: ["math","\u2063 arccos"," "],
    	4015: ["math","\u2063 arctan"," "],
    	4016: ["math","\u2063 max"," "],
    	4017: ["math","\u2063 min"," "],
    	4018: ["math","\u2063 floor"," "],
    	4019: ["math","\u2063 ceil"," "],
    	4020: ["HS_END_OF_MATH_OPERATORS"],
    	5000: ["ColorOperatorRandom"],
    	5001: ["color","\u2063 ","R","G","B"],
    	5002: ["color","\u2063 ","H","S","B"],
    	5003: ["HS_END_OF_COLOR_OPERATORS"],
    	6000: ["rule","When"],
    	6001: ["RulePreview"],
    	7000: ["event","\u2063 game starts \u2063"],
    	7001: ["event","is tapped \u2063"," "],
    	7002: ["event","is touching"],
    	7003: ["event","is pressed \u2063"," "],
    	7004: ["event","Tilted Right"],
    	7005: ["event","Tilted Left"],
    	7006: ["event","Tilted Up"],
    	7007: ["event","Tilted Down"],
    	7008: ["event","\u2063 \uD83D\uDCF1 hears a loud noise \u2063"],
    	7009: ["event","\u2063 \uD83D\uDCF1 is shaken \u2063"],
    	7010: ["event","bumps"],
    	7011: ["event","is swiped right \u2063"," "],
    	7012: ["event","is swiped left \u2063"," "],
    	7013: ["event","is swiped up \u2063"," "],
    	7014: ["event","is swiped down \u2063"," "],
    	7015: ["event","\u2063 object is cloned \u2063"],
    	7016: ["event","Editor Tilt Right \u2063"],
    	7017: ["event","Editor Tilt Left \u2063"],
    	7018: ["event","Editor Tilt Up \u2063"],
    	7019: ["event","Editor Tilt Down \u2063"],
    	7020: ["event","is not pressed \u2063"," "],
    	7021: ["event","\u2063 game is playing \u2063"],
    	7022: ["event","\u2063 touch ends \u2063"],
    	7023: ["event","\u2063 I get a message \u2063"],
    	7024: ["event","\u2063 Message matches \u2063"],
    	7025: ["event","is not touching"],
    	7026: ["HS_END_OF_EVENT_OPERATORS"],
    	8000: ["(Object)"],
    	8001: ["Anything"],
    	8002: ["???? Edge"],
    	8003: ["??????"],
    	8004: ["Self"],
    	8005: ["Original Object"],
    	8006: ["????"],
    	8007: ["????"],
    	8008: ["????"],
    	8009: ["Local "],
    	8010: ["HS_END_OF_EVENT_PARAMETER_BLOCKS"],
    	9000: ["looks", "\u2063 character", "in", "at"],
    	9001: ["looks", "\u2063 characters", "in", "between", "and"],
    	9002: ["looks", "\u2063 length"],
    	9003: ["HS_END_OF_TEXT_OPERATOR_BLOCKS"]
    }
""".trimIndent(), Map::class.java
)