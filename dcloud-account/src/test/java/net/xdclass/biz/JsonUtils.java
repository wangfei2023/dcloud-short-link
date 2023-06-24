package net.xdclass.biz;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

    public class JsonUtils {

        /**
         * 将树状结构的JSON数据转换为平级展示的JSON数据
         *
         * @param sourceJson 树状结构的JSON数据
         * @return 平级展示的JSON数据
         */
        public static JSONArray convertTreeToFlatJson(JSONArray sourceJson) {
            JSONArray flatJson = new JSONArray();
            parseJsonArray(sourceJson, "", 0, flatJson);
            return flatJson;
        }

        private static void parseJsonArray(JSONArray jsonArray, String parentPath, int level, JSONArray flatJson) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                // 提取当前节点的属性
                JSONObject node = new JSONObject();
                node.putAll(jsonObject);
                node.remove("children"); // 移除子节点信息

                // 添加节点层级信息
                node.put("level", level);
                String currentPath = parentPath + "/" + jsonObject.getString("name");
                node.put("path", currentPath);

                // 将节点加入到平级展示的JSON中
                flatJson.add(node);

                // 处理子节点信息
                JSONArray childrenJsonArray = jsonObject.getJSONArray("children");
                if (childrenJsonArray != null && !childrenJsonArray.isEmpty()) {
                    parseJsonArray(childrenJsonArray, currentPath, level + 1, flatJson);
                }
            }
        }
    }

