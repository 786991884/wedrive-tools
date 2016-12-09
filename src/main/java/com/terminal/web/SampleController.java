package com.terminal.web;

import com.terminal.cache.CommandCache;
import com.terminal.cache.RateStatistics;
import com.terminal.communication.MessageSend;
import com.terminal.func.GenerateTerminalData;
import com.terminal.mbean.IoAcceptorStat;
import com.terminal.netty.NettyClientHandler;
import com.terminal.netty.NettyClientServer;
import com.terminal.service.TestService;
import com.terminal.util.ConfigUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@Description("A controller for handling requests for hello messages")
public class SampleController {

    @Autowired
    private TestService testService;

    @Autowired
    private CommandCache commandCache;

    @Autowired
    private ConfigUtils configUtils;

    @Autowired
    private IoAcceptorStat ioAcceptorStat;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, String> test() {
        return Collections.singletonMap("message", this.testService.getTestMessage());
    }

    @RequestMapping(value = "/qpsmap", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> qpsmap() {
        Map<String, Object> model = new LinkedHashMap<>();
        long totalMapSize_5 = CommandCache.totalMapSize_5.get();
        long totalMapSize_10 = CommandCache.totalMapSize_10.get();
        long totalMapSize_15 = CommandCache.totalMapSize_15.get();
        long totalMapSize_30 = CommandCache.totalMapSize_30.get();
        long expireMapSize_5 = CommandCache.expireMapSize_5.get();
        long expireMapSize_10 = CommandCache.expireMapSize_10.get();
        long expireMapSize_15 = CommandCache.expireMapSize_15.get();
        long expireMapSize_30 = CommandCache.expireMapSize_30.get();
        long explicitMapSize_5 = CommandCache.explicitMapSize_5.get();
        long explicitMapSize_10 = CommandCache.explicitMapSize_10.get();
        long explicitMapSize_15 = CommandCache.explicitMapSize_15.get();
        long explicitMapSize_30 = CommandCache.explicitMapSize_30.get();
        long sendTotal = MessageSend.sendTotalPacketNum.get();
        long reveiveTotal = NettyClientHandler.reveiveTotalPacketNum.get();
        int expireTime_5 = configUtils.getExpireTime_5();
        int expireTime_10 = configUtils.getExpireTime_10();
        int expireTime_15 = configUtils.getExpireTime_15();
        int expireTime = configUtils.getExpireTime();

        // 创建一个数值格式化对象
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(2);
        String result = numberFormat.format((float) expireMapSize_5 / (float) sendTotal * 100);
        String result_10 = numberFormat.format((float) expireMapSize_10 / (float) sendTotal * 100);
        String result_15 = numberFormat.format((float) expireMapSize_15 / (float) sendTotal * 100);
        String result_30 = numberFormat.format((float) expireMapSize_30 / (float) sendTotal * 100);

        String result2 = numberFormat.format((float) explicitMapSize_5 / (float) sendTotal * 100);
        String result2_10 = numberFormat.format((float) explicitMapSize_10 / (float) sendTotal * 100);
        String result2_15 = numberFormat.format((float) explicitMapSize_15 / (float) sendTotal * 100);
        String result2_30 = numberFormat.format((float) explicitMapSize_30 / (float) sendTotal * 100);

        String result3 = numberFormat.format((float) (sendTotal - expireMapSize_5 - explicitMapSize_5) / (float) sendTotal * 100);
        String result3_10 = numberFormat.format((float) (sendTotal - expireMapSize_10 - explicitMapSize_10) / (float) sendTotal * 100);
        String result3_15 = numberFormat.format((float) (sendTotal - expireMapSize_15 - explicitMapSize_15) / (float) sendTotal * 100);
        String result3_30 = numberFormat.format((float) (sendTotal - expireMapSize_30 - explicitMapSize_30) / (float) sendTotal * 100);

        model.put("发送总包数", sendTotal);
        model.put("接收总包数", reveiveTotal);
        model.put("暂未接收包数", sendTotal - reveiveTotal);
        model.put("校验总包数" + expireTime_5 + "ms统计", totalMapSize_5);
        model.put("校验总包数" + expireTime_10 + "ms统计", totalMapSize_10);
        model.put("校验总包数" + expireTime_15 + "ms统计", totalMapSize_15);
        model.put("校验总包数" + expireTime + "ms统计", totalMapSize_30);
        model.put("超时包数" + expireTime_5 + "ms统计/" + "超时响应百分比" + expireTime_5 + "ms统计", expireMapSize_5 + "/" + result + "%");
        model.put("超时包数" + expireTime_10 + "ms统计/" + "超时响应百分比" + expireTime_10 + "ms统计", expireMapSize_10 + "/" + result_10 + "%");
        model.put("超时包数" + expireTime_15 + "ms统计/" + "超时响应百分比" + expireTime_15 + "ms统计", expireMapSize_15 + "/" + result_15 + "%");
        model.put("超时包数" + expireTime + "ms统计/" + "超时响应百分比" + expireTime + "ms统计", expireMapSize_30 + "/" + result_30 + "%");
        model.put("正常响应包数" + expireTime_5 + "ms统计/" + "正常响应百分比" + expireTime_5 + "ms统计", explicitMapSize_5 + "/" + result2 + "%");
        model.put("正常响应包数" + expireTime_10 + "ms统计/" + "正常响应百分比" + expireTime_10 + "ms统计", explicitMapSize_10 + "/" + result2_10 + "%");
        model.put("正常响应包数" + expireTime_15 + "ms统计/" + "正常响应百分比" + expireTime_15 + "ms统计", explicitMapSize_15 + "/" + result2_15 + "%");
        model.put("正常响应包数" + expireTime + "ms统计/" + "正常响应百分比" + expireTime + "ms统计", explicitMapSize_30 + "/" + result2_30 + "%");
        model.put("未应答包数" + expireTime_5 + "ms统计/" + "未应答百分比" + expireTime_5 + "ms统计", (sendTotal - expireMapSize_5 - explicitMapSize_5) + "/" + result3 + "%");
        model.put("未应答包数" + expireTime_10 + "ms统计/" + "未应答百分比" + expireTime_10 + "ms统计", (sendTotal - expireMapSize_10 - explicitMapSize_10) + "/" + result3_10 + "%");
        model.put("未应答包数" + expireTime_15 + "ms统计/" + "未应答百分比" + expireTime_15 + "ms统计", (sendTotal - expireMapSize_15 - explicitMapSize_15) + "/" + result3_15 + "%");
        model.put("未应答包数" + expireTime + "ms统计/" + "未应答百分比" + expireTime + "ms统计", (sendTotal - expireMapSize_30 - explicitMapSize_30) + "/" + result3_30 + "%");

        String result4 = numberFormat.format((float) (sendTotal - reveiveTotal) / (float) sendTotal * 100);
        model.put("暂未接收包数百分比", result4 + "%");
        model.put("tcp连接成功次数", NettyClientServer.connsuccNum);
        model.put("tcp连接失败次数", NettyClientServer.connfailNum);
        model.put("tcp触发重连次数", NettyClientServer.reconnNum);
        model.put("tcp发起重连次数", NettyClientServer.connsuccNum.get() - configUtils.getTerminalNumber());

        model.put("打压开始时间", GenerateTerminalData.start.toString());
        model.put("打压结束时间", GenerateTerminalData.end.toString());
        model.put("当前时间", (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()));
        return model;
    }

    @RequestMapping(value = "/flowmonitor", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> flowmonitor() {
        Map<String, Object> model = new LinkedHashMap<>();
        String s = new StringBuilder(300).append("监控 ").append("上下行流量")
                .append(" 当前读取速度: ").append(ioAcceptorStat.lastReadThroughput() >> 10).append(" KB/s, ")
                .append("回写速度: ").append(ioAcceptorStat.lastWriteThroughput() >> 10).append(" KB/s, ")
                .append("真实回写速度: ").append(ioAcceptorStat.getRealWriteThroughput() >> 10).append(" KB/s, ")
                .append("当前读取总量: ").append(ioAcceptorStat.currentReadBytes() >> 10).append(" KB, ")
                .append("当前回写总量: ").append(ioAcceptorStat.currentWrittenBytes() >> 10).append(" KB, ")
                .append("当前真实的回写总量: ").append(ioAcceptorStat.getRealWrittenBytes().get() >> 10).append(" KB, ")
                .append("总读取流量：").append(ioAcceptorStat.cumulativeReadBytes() >> 10).append(" KB, ")
                .append("总回写流量：").append(ioAcceptorStat.cumulativeWrittenBytes() >> 10).append("KB")
                .toString();
        //model.put("io使用情况", ioAcceptorStat.toString());
        model.put("io情况使用中文", s);
        model.put("当前读取速度", (ioAcceptorStat.lastReadThroughput() >> 10) + " KB/s");
        model.put("回写速度", (ioAcceptorStat.lastWriteThroughput() >> 10) + " KB/s");
        model.put("真实回写速度", (ioAcceptorStat.getRealWriteThroughput() >> 10) + " KB/s");
        model.put("当前读取总量", (ioAcceptorStat.currentReadBytes() >> 10) + " KB");
        model.put("当前回写总量", (ioAcceptorStat.currentWrittenBytes() >> 10) + " KB");
        model.put("当前真实的回写总量", (ioAcceptorStat.getRealWrittenBytes().get() >> 10) + " KB");
        model.put("总读取流量", (ioAcceptorStat.cumulativeReadBytes() >> 10) + " KB");
        model.put("总回写流量", (ioAcceptorStat.cumulativeWrittenBytes() >> 10) + " KB");
        model.put("发包速率1s/读包速率1s", RateStatistics.sendResult1s + "/s" + "------" + RateStatistics.readResult1s + "/s");
        model.put("发包速率2s/读包速率2s", RateStatistics.sendResult2s + "/s" + "------" + RateStatistics.readResult2s + "/s");
        model.put("发包速率3s/读包速率3s", RateStatistics.sendResult3s + "/s" + "------" + RateStatistics.readResult3s + "/s");
        model.put("发包速率5s/读包速率5s", RateStatistics.sendResult5s + "/s" + "------" + RateStatistics.readResult5s + "/s");
        model.put("发包速率10s/读包速率10s", RateStatistics.sendResult10s + "/s" + "------" + RateStatistics.readResult10s + "/s");
        model.put("当前时间", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return model;
    }


    @RequestMapping(value = "/sendReveiveChart", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> sendReveiveChart(@RequestParam(required = false, defaultValue = "1") Integer s) {
        Map<String, Object> model = new LinkedHashMap<>();
        //model.put("发包速率", Math.ceil(Double.parseDString, Objectouble(RateStatistics.sendResult)) + "/s");
        //model.put("读包速率", Math.ceil(Double.parseDouble(RateStatistics.readResult)) + "/s");
        if (s == 1) {
            model.put("发包速率每" + s + "s统计", RateStatistics.chartMap_1);
            model.put("读包速率每" + s + "s统计", RateStatistics.chartMapS_1);
        } else if (s == 2) {
            model.put("发包速率每" + s + "s统计", RateStatistics.chartMap_2);
            model.put("读包速率每" + s + "s统计", RateStatistics.chartMapS_2);
        } else if (s == 3) {
            model.put("发包速率每" + s + "s统计", RateStatistics.chartMap_3);
            model.put("读包速率每" + s + "s统计", RateStatistics.chartMapS_3);
        } else if (s == 5) {
            model.put("发包速率每" + s + "s统计", RateStatistics.chartMap_5);
            model.put("读包速率每" + s + "s统计", RateStatistics.chartMapS_5);
        } else if (s == 10) {
            model.put("发包速率每" + s + "s统计", RateStatistics.chartMap_10);
            model.put("读包速率每" + s + "s统计", RateStatistics.chartMapS_10);
        }
        model.put("date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return model;
    }


    @RequestMapping(value = "/sendChart", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> sendChart(@RequestParam(required = false, defaultValue = "1") Integer s) {
        Map<String, Object> model = new LinkedHashMap<>();
        if (s == 1) {
            model.put("发包速率每" + s + "s统计", RateStatistics.chartMap_1);
        } else if (s == 2) {
            model.put("发包速率每" + s + "s统计", RateStatistics.chartMap_2);
        } else if (s == 3) {
            model.put("发包速率每" + s + "s统计", RateStatistics.chartMap_3);
        } else if (s == 5) {
            model.put("发包速率每" + s + "s统计", RateStatistics.chartMap_5);
        } else if (s == 10) {
            model.put("发包速率每" + s + "s统计", RateStatistics.chartMap_10);
        }
        model.put("date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return model;
    }

    @RequestMapping(value = "/reveiveChart", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> reveiveChart(@RequestParam(required = false, defaultValue = "1") Integer s) {
        Map<String, Object> model = new LinkedHashMap<>();
        if (s == 1) {
            model.put("读包速率每" + s + "s统计", RateStatistics.chartMapS_1);
        } else if (s == 2) {
            model.put("读包速率每" + s + "s统计", RateStatistics.chartMapS_2);
        } else if (s == 3) {
            model.put("读包速率每" + s + "s统计", RateStatistics.chartMapS_3);
        } else if (s == 5) {
            model.put("读包速率每" + s + "s统计", RateStatistics.chartMapS_5);
        } else if (s == 10) {
            model.put("读包速率每" + s + "s统计", RateStatistics.chartMapS_10);
        }
        model.put("date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return model;
    }

   /* @RequestMapping(value = "/test3", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> chart( Message message) {
        Map<String, Object> model = new LinkedHashMap<String, Object>();
        model.put("message", message.getValue());
        model.put("title", "Hello Home");
        model.put("date", new Date());
        return model;
    }
*/

    /*@RequestMapping(value = "/test2", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> olleh(@Validated Message message) {
        Map<String, Object> model = new LinkedHashMap<String, Object>();
        model.put("message", message.getValue());
        model.put("title", "Hello Home");
        model.put("date", new Date());
        return model;
    }

    @RequestMapping("/foo")
    @ResponseBody
    public String foo() {
        throw new IllegalArgumentException("Server error");
    }

    private static class Message {

        @NotBlank(message = "Message value cannot be empty")
        private String value;

        public String getValue() {
            return this.value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }*/

}
