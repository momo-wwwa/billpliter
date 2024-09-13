package billsplitter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/BillSplitterServlet")
public class BillSplitterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public BillSplitterServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//int n; // 人数
		int b; // 請求額
		int t;//支払単位
		int n1;//第一グループの人数
		int n2;//第二グループの人数
		double r;//倍率

		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();

		String paramB = request.getParameter("b");
		String paramN1 = request.getParameter("n1");
		String paramN2 = request.getParameter("n2");
		String paramT = request.getParameter("t");
		String paramR = request.getParameter("r");
		if (paramB == null || paramN1 == null || paramT == null || paramN1 == null || paramN2 == null || paramR == null) {
			printInitialPage(out);
			return;
		}

		try {
			b = Integer.parseInt(paramB);
			n1 = Integer.parseInt(paramN1);
			n2 = Integer.parseInt(paramN2);
			t = Integer.parseInt(paramT);
			r = Float.parseFloat(paramR);
		} catch (java.lang.NumberFormatException e) {
			out.println("入力しなおせ");
			out.println("history.back();");
			return;
		}

		printResultPage(out, b, n1, n2, t, r);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	void printInitialPage(PrintWriter out) {
		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<head>");
		out.println("<meta charset=\"UTF-8\">");
		out.println("<title>割り勘計算</title>");
		out.println("</head>");
		out.println("<body>");
		//見出しの設定。囲って、真ん中寄せにした。
		out.println("<div style=\" background: #ffd6ad; padding: 20px; border: 3px dashed #fff9f4; box-shadow: 0 0 0 4px #ffd6ad; -moz-box-shadow: 0 0 0 4px #ffd6ad; -webkit-box-shadow: 0 0 0 4px #ffd6ad; font-size: 100%;\"><h1 style=\"text-align:center\">割り勘計算</h1></div><br>");
		out.println("<form action=\"BillSplitterServlet\" method=\"get\">");
		//倍率や、支払い単位についての説明
		out.println("<div  style=\"position: relative;  margin: 2em 0 2em 40px; background: #ffd6ad; border-radius: 20px; font-size: 100%; padding: 20px;\"><span  style=\"  position: absolute; left: -38px; width: 13px; height: 12px; bottom: 0; background: #ffd6ad; border-radius: 50%; \"></span><p>使えるのは半角数字のみ</p>倍率とは：第一グループに対する第二グループの倍率を指定する<p><p>支払い単位とは：1人あたりの支払額をどの単位で計算するかを指定します。1人あたりの支払額がこの単位の整数倍</p><span style=\" position: absolute; left: -24px; width: 20px; height: 18px;  bottom: 3px; background: #ffd6ad; border-radius: 50%;  margin: 0;  padding: 0;\"></span></div><br>");
		//入力フォームを区切った。text-align: centerを加えることで、真ん中寄席にした。
		out.println("<div style=\"text-align: center\"><div style=\"border-radius: 5px; border: 3px dashed #ffd6ad ;font-size: 100%; padding: 20px;\">");
		/*input内でrequiredをつけることにより、入力が必須となった。
		 pattern=\"^[0-9]+$\" name=\"b\"をつけることによって、半角数字以外入力できないようになった。title=\"半角数字を加えて、適切でない値が入力されたとき、半角数字を使うように促している*/
		out.println("請求額: <input type=\"text\" pattern=\"^[0-9]+$\" name=\"b\" title=\"半角数字\" required>円<br><br>");
		out.println("第一グループ: <input type=\"text\" pattern=\"^[0-9]+$\" name=\"n1\" title=\"半角数字\" required>人<br><br>");
		out.println("第二グループ: <input type=\"text\" pattern=\"^[0-9]+$\" name=\"n2\" title=\"半角数字\" required>人<br><br>");
		out.println("倍率: <input type=\"text\" pattern=\"^[0-9]+$\" name=\"r\" title=\"半角数字\" required><br><br>");
		out.println("支払い単位:");
		//支払い単位をある程度制限する、さらにタブ入力することで入力の手間が省ける
		out.println("<select name=\"t\" required>");
		out.println("<option value=\"\" hidden>選択</option>");
		out.println("<option value=\"1\">1円</option>");
		out.println("<option value=\"10\">10円</option>");
		out.println("<option value=\"100\">100円</option>");
		out.println("<option value=\"1000\">1000円</option>");
		out.println("</select><br><br>");
		out.println("<input type=\"submit\">");
		out.println("</div></div>");
		out.println("</form>");
		out.println("</body>");
		out.println("</html>");
	}

	void printResultPage(PrintWriter out, int b, int n1, int n2, int t, double r) {
		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<head>");
		out.println("<meta charset=\"UTF-8\">");
		out.println("<title>割り勘計算</title>");
		out.println("</head>");
		out.println("<body>");
		//見出しの設定。囲って、真ん中寄せにした。
		out.println("<div style=\" background: #ffd6ad; padding: 20px; border: 3px dashed #fff9f4; box-shadow: 0 0 0 4px #ffd6ad; -moz-box-shadow: 0 0 0 4px #ffd6ad; -webkit-box-shadow: 0 0 0 4px #ffd6ad; font-size: 100%;\"><h1 style=\"text-align:center\">割り勘計算</h1></div><br>");
		//入力フォームを区切った
		out.println("<div style=\"border-radius: 5px; border: 3px dashed #ffd6ad ;font-size: 100%; padding: 20px;\">");
		out.println("請求額: " + b + "円<br><br>");
		out.println("第一グループ人の数: " + n1 + "人<br><br>");
		out.println("第二グループの人数: " + n2 + "人<br><br>");
		out.println("倍率: " + r + "<br><br>");
		out.println("支払い単位: " + t + "円<br><br>");
		
		double b2 =(double) b/t;
		double amari = b2 % (n1*r+n2);
		if (amari != 0) {
			double	payment1 =(b2-amari)/(n1*r + n2)*t;
			double payment2 = payment1*r;
			out.println("第1グループの1人の当たりの支払額: " + payment1+ "円<br><br>");
			out.println("第2グループの1人の当たりの支払額: " + payment2+ "円<br><br>");
		}else {
			double payment1 = b2 / (n1*r + n2)*t; // 1グループの一人当たりの支払
			double payment2 = payment1*r;
			out.println("第1グループの1人の1人当たりの支払額: " + payment1+ "円<br><br>");
			out.println("第2グループの1人の1人当たりの支払額: " + payment2+ "円<br><br>");
		}
		amari = amari*t;
		out.println("端数（不足分）: " + amari + "円<br><br>");
		//戻るを押すと、前回入力した情報も含まれた画面に戻る。
		out.println("<button onclick=\"history.back()\">戻る</button>");
		out.println("</div>");
		out.println("</body>");
		out.println("</html>");
	}
}
