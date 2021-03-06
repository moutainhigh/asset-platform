package cn.org.assetcloud.system.service.impl;

import cn.org.assetcloud.system.entity.OrganScene;
import cn.org.assetcloud.system.entity.OrganTree;
import cn.org.assetcloud.system.mapper.OrganSceneMapper;
import cn.org.assetcloud.system.mapper.OrganTreeMapper;
import cn.org.assetcloud.system.service.IOrganSceneService;
import cn.org.assetcloud.system.utils.CommonUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hjhu
 * @since 2019-07-23
 */
@Service
@AllArgsConstructor
public class OrganSceneServiceImpl extends ServiceImpl<OrganSceneMapper, OrganScene> implements IOrganSceneService {

    OrganSceneMapper organSceneMapper;

    OrganTreeMapper organTreeMapper;

    @Override
    public boolean batchRemove(List<String> nodeIds, String sceneId) {
        return organSceneMapper.delete(Wrappers.<OrganScene>query().lambda().in(OrganScene::getNodeId, nodeIds)
                .eq(OrganScene::getSceneId, sceneId)) > 0;
    }

    @Override
    public boolean batchRemoveMembers(List<String> userIds, String sceneId) {
        return organSceneMapper.batchDeleteByUserId(userIds, sceneId) > 0;
    }

    @Override
    public boolean addNodes(String treeIds, String sceneId) {
        // 删除已有节点
//        this.remove(Wrappers.<OrganScene>query().lambda().in(OrganScene::getNodeId, nodeIds)
//                .eq(OrganScene::getSceneId, sceneId));
        this.remove(Wrappers.<OrganScene>query().lambda().eq(OrganScene::getSceneId, sceneId));
        List<OrganTree> treeList = organTreeMapper.selectBatchIds(Func.toStrList(",", treeIds));
        ArrayList<OrganScene> organScenes = new ArrayList<>();
        for (OrganTree treeNode : treeList) {
            OrganScene node = CommonUtils.NodeTransformer(treeNode);
            node.setSceneId(sceneId);
            organScenes.add(node);
        }
        // 新增节点
        return this.saveBatch(organScenes);
    }
}
