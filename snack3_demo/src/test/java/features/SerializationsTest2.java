package features;

import _model4.QueryParamEntity;
import _models.UserGroupModel;
import _models.UserGroupModel2;
import _models.UserModel;
import org.junit.Test;
import org.noear.snack.ONode;
import org.noear.snack.core.TypeRef;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

public class SerializationsTest2 {

    public Object buildObj() {
        UserGroupModel group = new UserGroupModel();
        group.id = 9999;
        group.users = new ArrayList<>();
        group.users2 = new LinkedHashMap<>();
        group.users3 = new TreeSet<>();
        group.names = new String[5];
        group.ids = new short[5];
        group.iids = new Integer[5];
        group.dd = new BigDecimal(12);
        group.tt1 = new Timestamp(new Date().getTime());
        group.tt2 = new Date();

        for (short i = 0; i < 5; i++) {
            UserModel user = new UserModel();
            user.id = i;
            user.name = "张三" + i;
            user.note = null;
            group.users.add(user);
            group.users2.put(Integer.valueOf(i), user);
            group.names[i] = "李四" + i;
            group.ids[i] = i;
        }

        return group;
    }

    public String buildJson() {
        return ONode.loadObj(buildObj()).toJson();
    }

    @Test
    public void test01() {
        String tmp = ONode.serialize(buildObj());
        System.out.println(tmp);
    }

    @Test
    public void test02() {
        String tmp = ONode.serialize(buildObj());
        tmp = tmp.replaceAll("UserGroupModel", "UserGroupModel2");
        UserGroupModel2 tmp2 = ONode.deserialize(tmp, UserGroupModel2.class);

        assert tmp2.users != null;
        assert tmp2.users.length > 2;
        System.out.println(tmp2);
    }

    @Test
    public void test10() throws Exception {
        String json0 = buildJson();

        System.out.println(json0);
        UserGroupModel group0 = ONode.loadStr(json0)
                .toObject((new TypeRef<UserGroupModel>() {
                }).getClass());

        assert group0.id == 9999;
    }

    @Test
    public void test11() throws Exception {
        String json0 = buildJson();

        System.out.println(json0);
        UserGroupModel group0 = ONode.loadStr(json0)
                .toObject(UserGroupModel.class);

        assert group0.id == 9999;
    }

    @Test
    public void test20() throws Exception {
        String json0 = buildJson();

        System.out.println(json0);
        List<UserModel> group0 = ONode.loadStr(json0).get("users")
                .toObject((new ArrayList<UserModel>() {
                }).getClass());

        assert group0.size() == 5;
    }

    @Test
    public void test21() throws Exception {
        String json0 = buildJson();

        System.out.println(json0);
        List<UserModel> group0 = ONode.loadStr(json0).get("users")
                .toObject((new TypeRef<List<UserModel>>() {
                }).getClass());

        assert group0.size() == 5;
    }

    @Test
    public void test3() {
        String queryString = "pageIndex=0&pageSize=10&sorts[0].name=time&sorts[0].order=desc&terms[0].column=source&terms[0].value=SciVault&terms[1].column=descriptionFilter$LIKE&terms[1].value=%25aaa%25&terms[2].column=time$btw&terms[2].value=1660492800000,1661184000000&excludes=return_filters";
        String[] kvAry = queryString.split("&");
        Properties props = new Properties();

        for (String kvStr : kvAry) {
            String[] kv = kvStr.split("=");
            props.setProperty(kv[0], kv[1]);
        }

        ONode oNode = ONode.loadObj(props);

        System.out.println(oNode.toJson());

        QueryParamEntity entity = oNode.toObject(QueryParamEntity.class);

        assert entity != null;
        assert entity.getPageIndex() == 0;
        assert entity.getPageSize() == 10;
        assert entity.getSorts().size() > 0;
        assert entity.getTerms().size() > 0;
        assert entity.getExcludes().size() == 1;
    }
}
