package fun.lib.actor.example;

import fun.lib.actor.api.DFTcpChannel;
import fun.lib.actor.core.DFActor;
import fun.lib.actor.core.DFActorDefine;
import fun.lib.actor.core.DFActorManager;
import fun.lib.actor.po.DFTcpServerCfg;

/**
 * websocket服务示例
 * @author lostsky
 *
 */
public final class WebsocketServer {

	public static void main(String[] args){
		final DFActorManager mgr = DFActorManager.get();
		//启动入口actor，开始消息循环		
		mgr.start(Server.class);
	}
	
	private static class Server extends DFActor{
		public Server(Integer id, String name, Boolean isBlockActor) {
			super(id, name, isBlockActor);
			// TODO Auto-generated constructor stub
		}
		private final int serverPort = 10001;
		@Override
		public void onStart(Object param) {
			DFTcpServerCfg cfg = new DFTcpServerCfg(serverPort, 1, 1)
				.setTcpProtocol(TCP_PROTOCOL_WEBSOCKET)
				.setWsUri("test");  //如 ws://127.0.0.1:10001/test
			log.info("onStart, ready to listen on port "+serverPort);
			//启动端口监听
			net.tcpSvr(cfg);
		}
		@Override
		public void onTcpConnOpen(int requestId, DFTcpChannel channel) {
			final int channelId = channel.getChannelId();
			log.info("onTcpConnOpen, remote="+channel.getRemoteHost()+":"+channel.getRemotePort()
				+", channelId="+channelId);
		}
		@Override
		public void onTcpConnClose(int requestId, DFTcpChannel channel) {
			final int channelId = channel.getChannelId();
			log.info("onTcpConnClose, remote="+channel.getRemoteHost()+":"+channel.getRemotePort()
				+", channelId="+channelId);
		}
		@Override
		public int onTcpRecvMsg(int requestId, DFTcpChannel channel, Object m) {
			String msg = (String) m;
			//response
			channel.write("echo from server: "+msg);
			//消息对象交由框架释放
			return MSG_AUTO_RELEASE; 
		}
		@Override
		public void onTcpServerListenResult(int requestId, boolean isSucc, String errMsg) {
			log.info("onTcpServerListenResult, port="+requestId+", succ="+isSucc+", err="+errMsg);
		}
	}
}
