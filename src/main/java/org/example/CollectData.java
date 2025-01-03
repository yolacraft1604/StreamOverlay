package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CollectData {
    private double Percent = 0; //
    private int Distance = 0; //
    private int SHx = 0; //
    private int SHz = 0; //
    private int Eyethrows = 0; //
    private int Boatstate = 0;
    // 0 = gray, 1 = blue, 2 = green, 3 = red
    private int ChunkX = 0; //
    private int ChunkZ = 0; //
    private Boolean Nether = false; //

    public CollectData() {}

    public void UpdateData() {
        ApiCalls BoatAPI = new ApiCalls("/api/v1/boat");
        ApiCalls StrongholdAPI = new ApiCalls("/api/v1/stronghold");

        try {
            BoatAPI.Call();
            StrongholdAPI.Call();

            // Nether
            Object Temp = StrongholdAPI.getData("playerPosition.isInNether");
            Nether = Temp instanceof Boolean ? (Boolean) Temp : false;

            // Eye throws
            Temp = StrongholdAPI.getData("eyeThrows");
            if (Temp instanceof JSONArray) {
                Eyethrows = ((JSONArray) Temp).length();
            } else {
                Eyethrows = 0; // Default value
            }

            // Distance
            Temp = StrongholdAPI.getData("predictions.0.overworldDistance");
            if (Temp instanceof BigDecimal) {
                Distance = ((BigDecimal) Temp).intValue();
            } else {
                Distance = 0; // Default value
            }

            // SH location (x and z)
            Temp = StrongholdAPI.getData("predictions.0.chunkX");
            if (Temp instanceof Integer) {
                ChunkX = (Integer) Temp;
                SHx = ChunkX * 16 + 4;
            } else {
                ChunkX = 0;
                SHx = 0;
            }

            Temp = StrongholdAPI.getData("predictions.0.chunkZ");
            if (Temp instanceof Integer) {
                ChunkZ = (Integer) Temp;
                SHz = ChunkZ * 16 + 4;
            } else {
                ChunkZ = 0;
                SHz = 0;
            }

            if (Nether) {
                Distance /= 8;
                SHx = SHx < 0 ? SHx / 8 - 1 : SHx / 8;
                SHz = SHz < 0 ? SHz / 8 - 1 : SHz / 8;
            }

            // Certainty (percent)
            Temp = StrongholdAPI.getData("predictions.0.certainty");
            if (Temp instanceof BigDecimal) {
                Percent = ((BigDecimal) Temp).doubleValue();
                Percent = Percent*1000;
                Percent = Math.round(Percent);
                Percent = Percent / 10;
            } else if (Temp instanceof Double) {
                Percent = (Double) Temp * 100;
            }else if(Temp instanceof Integer){
                Percent = (Integer) Temp * 100;
            }else {
                Percent = 0;
            }

            // Boat state
            Temp = BoatAPI.getData("boatState");
            if (Temp instanceof String) {
                switch ((String) Temp) {
                    case "NONE":
                        Boatstate = 0;
                        break;
                    case "MEASURING":
                        Boatstate = 1;
                        break;
                    case "VALID":
                        Boatstate = 2;
                        break;
                    case "ERROR":
                        Boatstate = 3;
                        break;
                    default:
                        Boatstate = 0; // Default value
                }
            } else {
                Boatstate = 0; // Default value
            }

        } catch (Exception e) {
            // Log the error for debugging
            System.err.println("Error updating data: " + e.getMessage());
            e.printStackTrace();

            // Reset values to defaults
            Percent = 0;
            Distance = 0;
            SHx = 0;
            SHz = 0;
            Eyethrows = 0;
            Boatstate = 0;
            ChunkX = 0;
            ChunkZ = 0;
            Nether = false;
        }
    }

    public int getDistance() {
        return Distance;
    }

    public int getSHx() {
        return SHx;
    }

    public int getSHz() {
        return SHz;
    }

    public int getEyethrows() {
        return Eyethrows;
    }

    public int getBoatstate() {
        return Boatstate;
    }

    public int getChunkX() {
        return ChunkX;
    }

    public int getChunkZ() {
        return ChunkZ;
    }

    public Boolean getNether() {
        return Nether;
    }

    public double getPercent() {
        return Percent;
    }
}
