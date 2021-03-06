package team.chisel.client.render.texture;

import java.util.Collections;
import java.util.List;

import net.minecraft.client.renderer.block.model.BakedQuad;
import team.chisel.api.render.IBlockRenderContext;
import team.chisel.api.render.TextureInfo;
import team.chisel.client.render.Quad;
import team.chisel.client.render.ctm.CTM;
import team.chisel.client.render.ctm.ISubmap;
import team.chisel.client.render.ctx.CTMBlockRenderContext;
import team.chisel.client.render.type.BlockRenderTypeSimpleCTM;
import team.chisel.common.util.Dir;

public class ChiselTextureSimpleCTM extends AbstractChiselTexture<BlockRenderTypeSimpleCTM> {

    public ChiselTextureSimpleCTM(BlockRenderTypeSimpleCTM type, TextureInfo info) {
        super(type, info);
    }

    @Override
    public List<BakedQuad> transformQuad(BakedQuad quad, IBlockRenderContext context, int quadGoal) {
        Quad q = Quad.from(quad).setFullbright(fullbright);

        if (context == null) {
            return Collections.singletonList(q.transformUVs(sprites[0].getSprite(), Quad.TOP_LEFT).rebake());
        }

        return Collections.singletonList(q.transformUVs(sprites[0].getSprite(), getQuad(((CTMBlockRenderContext) context).getCTM(quad.getFace()))).rebake());
    }

    private ISubmap getQuad(CTM ctm) {
        if (ctm == null) {
            return Quad.TOP_LEFT;
        }

        if (!ctm.connectedOr(Dir.TOP, Dir.RIGHT, Dir.BOTTOM, Dir.LEFT)) {
            return Quad.TOP_LEFT;
        } else if (ctm.connectedAnd(Dir.TOP, Dir.TOP_RIGHT, Dir.RIGHT, Dir.BOTTOM_RIGHT, Dir.BOTTOM, Dir.BOTTOM_LEFT, Dir.LEFT, Dir.TOP_LEFT)) {
            return Quad.BOTTOM_RIGHT;
        } else if (ctm.connectedAnd(Dir.TOP, Dir.RIGHT, Dir.BOTTOM, Dir.LEFT)) {
            return Quad.TOP_LEFT;
        } else if (ctm.connectedAnd(Dir.LEFT, Dir.RIGHT)) {
            return Quad.TOP_RIGHT;
        } else if (ctm.connectedAnd(Dir.TOP, Dir.BOTTOM)) {
            return Quad.BOTTOM_LEFT;
        } else {
            return Quad.TOP_LEFT;
        }
    }
}
