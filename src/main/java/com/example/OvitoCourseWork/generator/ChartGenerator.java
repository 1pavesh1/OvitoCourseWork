package com.example.OvitoCourseWork.generator;

import java.util.Map;

public class ChartGenerator {
    // Цвета для категорий
    public static final String[] CATEGORY_COLORS = {
            "#6e48aa", "#9d50bb", "#4776E6", "#8E54E9",
            "#FF416C", "#FF4B2B", "#38a169", "#2e7d32"
    };

    // Цвета для статусов
    public static final String[] STATUS_COLORS = {
            "#38a169", // В продаже (зеленый)
            "#FF416C"  // Продано (красный)
    };

    public static String generatePieChart(Map<String, Integer> data, String[] colors, int width, int height)
    {
        if (data == null || data.isEmpty())
        {
            return "<svg width='" + width + "' height='" + height + "'></svg>";
        }

        int total = data.values().stream().mapToInt(Integer::intValue).sum();
        if (total == 0)
        {
            return "<svg width='" + width + "' height='" + height + "'></svg>";
        }

        StringBuilder svg = new StringBuilder();
        svg.append("<svg width='").append(width).append("' height='").append(height)
                .append("' viewBox='0 0 ").append(width).append(" ").append(height)
                .append("' xmlns='http://www.w3.org/2000/svg'>");

        int centerX = width / 2;
        int centerY = height / 2;
        int radius = Math.min(width, height) / 2 - 10;

        double startAngle = 0;
        int colorIndex = 0;

        // Генерация секторов диаграммы
        for (Map.Entry<String, Integer> entry : data.entrySet())
        {
            double value = entry.getValue();
            double angle = (value / total) * 360;
            double endAngle = startAngle + angle;

            String color = colors[colorIndex % colors.length];
            colorIndex++;

            svg.append(generateSector(centerX, centerY, radius, startAngle, endAngle, color));
            startAngle = endAngle;
        }

        int legendX = 20;
        int legendY = height - 50;
        colorIndex = 0;

        for (Map.Entry<String, Integer> entry : data.entrySet())
        {
            String color = colors[colorIndex % colors.length];
            colorIndex++;

            svg.append("<rect x='").append(legendX).append("' y='").append(legendY)
                    .append("' width='15' height='15' fill='").append(color).append("'/>");

            svg.append("<text x='").append(legendX + 20).append("' y='").append(legendY + 12)
                    .append("' font-size='12' fill='#333'>")
                    .append(entry.getKey()).append(": ").append(entry.getValue())
                    .append("</text>");

            legendY += 20;
        }

        svg.append("</svg>");
        return svg.toString();
    }

    private static String generateSector(int centerX, int centerY, int radius,
                                         double startAngle, double endAngle, String color)
    {
        double startRad = Math.toRadians(startAngle - 90);
        double endRad = Math.toRadians(endAngle - 90);

        double x1 = centerX + radius * Math.cos(startRad);
        double y1 = centerY + radius * Math.sin(startRad);
        double x2 = centerX + radius * Math.cos(endRad);
        double y2 = centerY + radius * Math.sin(endRad);

        int largeArcFlag = (endAngle - startAngle) <= 180 ? 0 : 1;

        return "<path d='M" + centerX + "," + centerY +
                " L" + x1 + "," + y1 +
                " A" + radius + "," + radius + " 0 " + largeArcFlag + ",1 " + x2 + "," + y2 +
                " Z' fill='" + color + "' stroke='white' stroke-width='1'/>";
    }
}