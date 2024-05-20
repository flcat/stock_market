package com.flcat.stock_market.data;

import com.google.gson.Gson;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class PrintOpsData {
    public void printMessage(String message) {

        System.out.println("[RECV] :" + message);

        char fStr = message.charAt(0);

        // 첫데이터로 전문인지 json 데이터인지 구분을해서 처리를 해야한다.
        if (fStr == '0') {
            // 암호화 되지 않은 전문 처리
            String[] mData = message.split("\\|");
            String tr_id = mData[1];
            //System.out.println("trid : "+tr_id);

            switch (tr_id) {
                case "HDFSASP0":
                    System.out.println("[" + tr_id + "]" + "###################################################");
//                    System.out.println(message);
                    break;
                case "H0STCNT0":
                    System.out.println("[" + tr_id + "]" + "###################################################");
                    break;
                default:
                    break;
            }
        } else if (fStr == '1') {
            // 암호화 된 전문 처리, 주식체결통보 데이터만 암호화 되서 오므로 해당데이터만 처리
            String[] mData = message.split("\\|");
            String tr_id = mData[1];
            //System.out.println("trid : "+tr_id);

            if ("H0STCNI0".equals(tr_id)) {    // 주식체결통보(고객용)
                System.out.println("[" + tr_id + "]" + "###################################################");
            }

        } else {
            // 일반 json 처리
            JSONParser parser = new JSONParser();
            Object object;
            JSONObject jsonObj;
            Gson gson = new Gson();
            try {
                object = parser.parse(message);
                String jsonStr = gson.toJson(object);
                jsonObj = (JSONObject) parser.parse(jsonStr);
//                jsonObj = (JSONObject) obj;
                //System.out.println("================================");

                //System.out.println("[RECV] :"+jsonObj.toString());
                JSONObject header = (JSONObject) jsonObj.get("header");

                String tmp_key = "";
                String tmp_iv = "";

                // tr_id 로 데이터처리를 구분한다.
                String tr_id = header.get("tr_id").toString();
                System.out.println("tr id : " + tr_id);
                // 일반 요청에 대한 응답 데이터일 경우
//                if (tr_id.equals("PINGPONG")) {
                    // 일반 요청에 대한 응답 데이터에만 body 가 있다.
//                    System.out.println(jsonObj.get("body").toString());
//                    switch (tr_id) {
//                        case "HDFSASP0":    // 주식호가
////                            System.out.println("주식호가 [" + rt_msg + "] [" + msg + "]");
//                            System.out.println(jsonStr);
//                            break;
//                        case "H0STCNT0":
////                            System.out.println("주식체결 [" + rt_msg + "] [" + msg + "]");
//                            break;
//                        case "H0STCNI0":    // 주식체결통보(고객용)
//                        case "H0STCNI9":    // 주식체결통보(모의투자)
////                            System.out.println("주식체결통보 [" + rt_msg + "] [" + msg + "]");
////                            // 체결통보일 경우 복호화를 해야 하므로 key, iv를 저장해서 쓴다.
////                            clientEndpoint.Key = tmp_key;
////                            clientEndpoint.iv = tmp_iv;
////                            System.out.printf(">> [%s] : SAVE KEY[%s] IV[%s]\r\n", tr_id, tmp_key, tmp_iv);
//                            //stocksigningnotice(message);
//                            break;
//                        default:
//                            break;
//                    }
//                } else if (tr_id.equals("PINGPONG"))    // PINGPONG 데이터일 경우
//                    {
//                        clientEndpoint.sendMessage(message);
//                        System.out.println("[SEND] :" + message);
//                    }
//                }
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
