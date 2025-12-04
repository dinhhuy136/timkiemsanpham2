import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class SearchEngine extends JFrame {
    private JTextArea websiteListArea;
    private JTextField keyword1Field, keyword2Field, keyword3Field;
    private JTextArea resultArea;
    private JButton searchButton, priceSearchButton, addLinkButton;
    
    // Màu sắc hiện đại
    private final Color PRIMARY_COLOR = new Color(37, 99, 235);
    private final Color SECONDARY_COLOR = new Color(16, 185, 129);
    private final Color ACCENT_COLOR = new Color(245, 158, 11);
    private final Color BACKGROUND_COLOR = new Color(249, 250, 251);
    private final Color CARD_COLOR = Color.WHITE;
    private final Color TEXT_COLOR = new Color(31, 41, 55);
    private final Color BORDER_COLOR = new Color(229, 231, 235);
    
    // Dữ liệu mẫu
    private Map<String, List<String>> websiteData;
    private Map<String, List<Product>> productData;
    
    public SearchEngine() {
        setTitle("Tìm kiếm tin trên internet");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout(15, 15));
        
        initData();
        createUI();
        
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    // Class Product để lưu thông tin sản phẩm
    static class Product {
        String name;
        String price;
        String rating;
        String link;
        
        Product(String name, String price, String rating, String link) {
            this.name = name;
            this.price = price;
            this.rating = rating;
            this.link = link;
        }
    }
    
    private void initData() {
        websiteData = new HashMap<>();
        websiteData.put("https://dantri.com.vn", Arrays.asList(
            "con báo số 3 gây thiệt hại lớn",
            "báo số 3 từ ngày 22/5 tới",
            "thời tiết hôm nay"
        ));
        websiteData.put("https://facebook.com", Arrays.asList(
            "cập nhật trạng thái mới",
            "báo số 3 đang di chuyển"
        ));
        
        // Khởi tạo dữ liệu sản phẩm mẫu
        productData = new HashMap<>();
        productData.put("https://shopee.vn", Arrays.asList(
            new Product("Điện thoại Samsung Galaxy S23", "15,990,000 VNĐ", "4.8/5", "https://shopee.vn/product1"),
            new Product("Laptop Dell XPS 13", "25,500,000 VNĐ", "4.7/5", "https://shopee.vn/product2"),
            new Product("Tai nghe Sony WH-1000XM5", "7,990,000 VNĐ", "4.9/5", "https://shopee.vn/product3")
        ));
        productData.put("https://lazada.vn", Arrays.asList(
            new Product("Điện thoại Samsung Galaxy S23", "15,500,000 VNĐ", "4.6/5", "https://lazada.vn/product1"),
            new Product("Laptop Dell XPS 13", "25,000,000 VNĐ", "4.5/5", "https://lazada.vn/product2")
        ));
        productData.put("https://tiki.vn", Arrays.asList(
            new Product("Điện thoại Samsung Galaxy S23", "16,200,000 VNĐ", "4.7/5", "https://tiki.vn/product1"),
            new Product("Tai nghe Sony WH-1000XM5", "8,200,000 VNĐ", "4.8/5", "https://tiki.vn/product2")
        ));
    }
    
    private void createUI() {
        // Main container với padding
        JPanel mainContainer = new JPanel(new BorderLayout(15, 15));
        mainContainer.setBackground(BACKGROUND_COLOR);
        mainContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel bên trái - Danh sách web
        JPanel leftPanel = createStyledPanel();
        leftPanel.setLayout(new BorderLayout(10, 10));
        leftPanel.setPreferredSize(new Dimension(300, 0));
        
        JLabel websiteLabel = new JLabel("📋 Danh sách web");
        websiteLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        websiteLabel.setForeground(TEXT_COLOR);
        websiteLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        websiteListArea = new JTextArea();
        websiteListArea.setText("🌐 https://dantri.com.vn\n🌐 https://facebook.com\n🌐 https://vnexpress.net\n🌐 https://tuoitre.vn\n🌐 https://thanhnien.vn");
        websiteListArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        websiteListArea.setBackground(BACKGROUND_COLOR);
        websiteListArea.setForeground(TEXT_COLOR);
        websiteListArea.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_COLOR, 1, true),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        websiteListArea.setLineWrap(true);
        websiteListArea.setWrapStyleWord(true);
        
        // Nút thêm link
        JButton addWebsiteButton = new JButton("➕ Thêm link");
        addWebsiteButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        addWebsiteButton.setForeground(Color.WHITE);
        addWebsiteButton.setBackground(ACCENT_COLOR);
        addWebsiteButton.setFocusPainted(false);
        addWebsiteButton.setBorderPainted(false);
        addWebsiteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addWebsiteButton.addActionListener(e -> showAddLinkDialog());
        addWebsiteButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                addWebsiteButton.setBackground(ACCENT_COLOR.darker());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                addWebsiteButton.setBackground(ACCENT_COLOR);
            }
        });
        
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanel.setBackground(CARD_COLOR);
        topPanel.add(websiteLabel, BorderLayout.WEST);
        topPanel.add(addWebsiteButton, BorderLayout.EAST);
        
        leftPanel.add(topPanel, BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(websiteListArea), BorderLayout.CENTER);
        
        // Panel giữa - Từ khóa và nút tìm kiếm
        JPanel centerPanel = createStyledPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setPreferredSize(new Dimension(350, 0));
        
        JLabel searchTitle = new JLabel("🔍 Tìm kiếm");
        searchTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        searchTitle.setForeground(TEXT_COLOR);
        searchTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerPanel.add(searchTitle);
        centerPanel.add(Box.createVerticalStrut(20));
        
        // Từ khóa 1
        centerPanel.add(createKeywordPanel("Từ khóa 1", keyword1Field = createStyledTextField("báo số 3")));
        centerPanel.add(Box.createVerticalStrut(15));
        
        // Từ khóa 2
        centerPanel.add(createKeywordPanel("Từ khóa 2", keyword2Field = createStyledTextField("")));
        centerPanel.add(Box.createVerticalStrut(15));
        
        // Từ khóa 3
        centerPanel.add(createKeywordPanel("Từ khóa 3", keyword3Field = createStyledTextField("")));
        centerPanel.add(Box.createVerticalStrut(25));
        
        // Nút Tìm kiếm
        searchButton = createStyledButton("🔎 Tìm kiếm", PRIMARY_COLOR);
        searchButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        searchButton.addActionListener(e -> performSearch());
        centerPanel.add(searchButton);
        centerPanel.add(Box.createVerticalStrut(15));
        
        // Nút Tìm giá sản phẩm
        priceSearchButton = createStyledButton("💰 Tìm giá sản phẩm", SECONDARY_COLOR);
        priceSearchButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        priceSearchButton.addActionListener(e -> performPriceSearch());
        centerPanel.add(priceSearchButton);
        
        centerPanel.add(Box.createVerticalGlue());
        
        // Panel bên phải - Kết quả
        JPanel rightPanel = createStyledPanel();
        rightPanel.setLayout(new BorderLayout(10, 10));
        
        JLabel resultLabel = new JLabel("📊 Kết quả tìm kiếm");
        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        resultLabel.setForeground(TEXT_COLOR);
        resultLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        resultArea = new JTextArea();
        resultArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        resultArea.setBackground(BACKGROUND_COLOR);
        resultArea.setForeground(TEXT_COLOR);
        resultArea.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_COLOR, 1, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setText("Nhập từ khóa và nhấn 'Tìm kiếm' để bắt đầu...");
        
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(null);
        
        rightPanel.add(resultLabel, BorderLayout.NORTH);
        rightPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Thêm các panel vào main container
        mainContainer.add(leftPanel, BorderLayout.WEST);
        mainContainer.add(centerPanel, BorderLayout.CENTER);
        mainContainer.add(rightPanel, BorderLayout.EAST);
        
        add(mainContainer);
    }
    
    private JPanel createStyledPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_COLOR, 1, true),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        return panel;
    }
    
    private JTextField createStyledTextField(String text) {
        JTextField field = new JTextField(text);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(300, 40));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_COLOR, 1, true),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        // Hiệu ứng focus
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(PRIMARY_COLOR, 2, true),
                    BorderFactory.createEmptyBorder(7, 11, 7, 11)
                ));
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(BORDER_COLOR, 1, true),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));
            }
        });
        
        return field;
    }
    
    private JPanel createKeywordPanel(String label, JTextField field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(CARD_COLOR);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        labelComponent.setForeground(TEXT_COLOR);
        labelComponent.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(labelComponent);
        panel.add(Box.createVerticalStrut(5));
        panel.add(field);
        
        return panel;
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setPreferredSize(new Dimension(300, 50));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(bgColor, 1, true),
            BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));
        
        // Hiệu ứng hover
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    private void performSearch() {
        String keyword1 = keyword1Field.getText().trim();
        String keyword2 = keyword2Field.getText().trim();
        String keyword3 = keyword3Field.getText().trim();
        
        if (keyword1.isEmpty() && keyword2.isEmpty() && keyword3.isEmpty()) {
            resultArea.setText("⚠️ Vui lòng nhập ít nhất một từ khóa để tìm kiếm!");
            return;
        }
        
        StringBuilder results = new StringBuilder();
        results.append("🔍 Đang tìm kiếm...\n\n");
        int count = 0;
        
        for (Map.Entry<String, List<String>> entry : websiteData.entrySet()) {
            String website = entry.getKey();
            List<String> articles = entry.getValue();
            
            for (String article : articles) {
                boolean match = false;
                
                if (!keyword1.isEmpty() && article.toLowerCase().contains(keyword1.toLowerCase())) match = true;
                if (!keyword2.isEmpty() && article.toLowerCase().contains(keyword2.toLowerCase())) match = true;
                if (!keyword3.isEmpty() && article.toLowerCase().contains(keyword3.toLowerCase())) match = true;
                
                if (match) {
                    count++;
                    results.append("✓ ").append(article).append("\n");
                    results.append("   📍 Nguồn: ").append(website).append("\n\n");
                }
            }
        }
        
        if (count == 0) {
            resultArea.setText("❌ Không tìm thấy kết quả phù hợp\n\nVui lòng thử với từ khóa khác!");
        } else {
            resultArea.setText("✅ Tìm thấy " + count + " kết quả:\n\n" + results.toString());
        }
    }
    
    private void performPriceSearch() {
        String keyword = keyword1Field.getText().trim();
        if (keyword.isEmpty()) {
            resultArea.setText("⚠️ Vui lòng nhập từ khóa vào ô 'Từ khóa 1' để tìm giá sản phẩm!");
            return;
        }
        
        StringBuilder results = new StringBuilder();
        results.append("💰 Đang tìm giá sản phẩm: ").append(keyword).append("\n\n");
        results.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n");
        
        List<Product> foundProducts = new ArrayList<>();
        String bestPrice = null;
        String bestSite = null;
        
        for (Map.Entry<String, List<Product>> entry : productData.entrySet()) {
            String website = entry.getKey();
            List<Product> products = entry.getValue();
            
            for (Product product : products) {
                if (product.name.toLowerCase().contains(keyword.toLowerCase())) {
                    foundProducts.add(product);
                    
                    String icon = "🛒";
                    if (website.contains("lazada")) icon = "🛍️";
                    else if (website.contains("tiki")) icon = "📦";
                    else if (website.contains("sendo")) icon = "🏪";
                    
                    results.append(icon).append(" ").append(website).append("\n");
                    results.append("   📱 ").append(product.name).append("\n");
                    results.append("   💵 Giá: ").append(product.price).append("\n");
                    results.append("   ⭐ ").append(product.rating).append("\n");
                    results.append("   🔗 ").append(product.link).append("\n\n");
                    
                    // Tìm giá tốt nhất
                    String priceStr = product.price.replaceAll("[^0-9]", "");
                    if (!priceStr.isEmpty()) {
                        if (bestPrice == null || Long.parseLong(priceStr) < Long.parseLong(bestPrice)) {
                            bestPrice = priceStr;
                            bestSite = website;
                        }
                    }
                }
            }
        }
        
        if (foundProducts.isEmpty()) {
            resultArea.setText("❌ Không tìm thấy sản phẩm: " + keyword + "\n\n" +
                              "💡 Thử thêm link sản phẩm bằng nút '➕ Thêm link' ở danh sách web!");
        } else {
            results.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
            if (bestPrice != null) {
                results.append("💡 Giá tốt nhất: ").append(formatPrice(bestPrice)).append(" VNĐ tại ").append(bestSite);
            }
            resultArea.setText(results.toString());
        }
    }
    
    private String formatPrice(String price) {
        try {
            long num = Long.parseLong(price);
            return String.format("%,d", num);
        } catch (NumberFormatException e) {
            return price;
        }
    }
    
    private void showAddLinkDialog() {
        JDialog dialog = new JDialog(this, "Thêm link bán hàng", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(BACKGROUND_COLOR);
        
        // Tiêu đề
        JLabel titleLabel = new JLabel("🔗 Thêm sản phẩm mới");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(20));
        
        // Website URL
        JLabel websiteLabel = new JLabel("Website (VD: https://shopee.vn):");
        websiteLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        websiteLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JTextField websiteField = createStyledTextField("");
        contentPanel.add(websiteLabel);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(websiteField);
        contentPanel.add(Box.createVerticalStrut(15));
        
        // Tên sản phẩm
        JLabel productLabel = new JLabel("Tên sản phẩm:");
        productLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        productLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JTextField productField = createStyledTextField("");
        contentPanel.add(productLabel);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(productField);
        contentPanel.add(Box.createVerticalStrut(15));
        
        // Giá
        JLabel priceLabel = new JLabel("Giá (VD: 150,000 VNĐ):");
        priceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        priceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JTextField priceField = createStyledTextField("");
        contentPanel.add(priceLabel);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(priceField);
        contentPanel.add(Box.createVerticalStrut(15));
        
        // Link sản phẩm
        JLabel linkLabel = new JLabel("Link sản phẩm:");
        linkLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        linkLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JTextField linkField = createStyledTextField("");
        contentPanel.add(linkLabel);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(linkField);
        contentPanel.add(Box.createVerticalStrut(20));
        
        // Nút thêm
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JButton cancelButton = new JButton("Hủy");
        cancelButton.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cancelButton.addActionListener(e -> dialog.dispose());
        
        JButton addButton = createStyledButton("➕ Thêm", SECONDARY_COLOR);
        addButton.setPreferredSize(new Dimension(120, 40));
        addButton.addActionListener(e -> {
            String website = websiteField.getText().trim();
            String productName = productField.getText().trim();
            String price = priceField.getText().trim();
            String link = linkField.getText().trim();
            
            if (website.isEmpty() || productName.isEmpty() || price.isEmpty() || link.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, 
                    "⚠️ Vui lòng điền đầy đủ thông tin!", 
                    "Lỗi", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Thêm sản phẩm vào dữ liệu
            if (!productData.containsKey(website)) {
                productData.put(website, new ArrayList<>());
                // Cập nhật danh sách website
                String currentText = websiteListArea.getText();
                websiteListArea.setText(currentText + "\n🌐 " + website);
            }
            
            productData.get(website).add(new Product(productName, price, "⭐ Mới", link));
            
            JOptionPane.showMessageDialog(dialog, 
                "✅ Đã thêm sản phẩm thành công!", 
                "Thành công", 
                JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
        });
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(addButton);
        contentPanel.add(buttonPanel);
        
        dialog.add(contentPanel);
        dialog.setVisible(true);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SearchEngine());
    }
}
