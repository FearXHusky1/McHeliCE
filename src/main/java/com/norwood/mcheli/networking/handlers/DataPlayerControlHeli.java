package com.norwood.mcheli.networking.handlers;

import com.hbm.tileentity.IBufPacketReceiver;
import hohserg.elegant.networking.api.IByteBufSerializable;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter
@NoArgsConstructor
public class DataPlayerControlHeli extends PlayerControlBaseData {
    public int unhitchChainId = -1;
    public BladeStatus bladeStatus = BladeStatus.NONE;


    @SuppressWarnings("unused")
    public DataPlayerControlHeli(ByteBuf buf) {
        super(buf);
        this.unhitchChainId = buf.readInt();
        this.bladeStatus    = BladeStatus.values()[buf.readByte()];
    }

    @Override
    public void serialize(ByteBuf buf) {
        super.serialize(buf); // serialize all base class fields

        buf.writeInt(unhitchChainId);
        buf.writeByte(bladeStatus.ordinal());
    }



    public static enum BladeStatus{
        NONE,UNFOLD,FOLD
    }
}
