package com.kutub.smarterp.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import com.kutub.smarterp.data.model.SaleEntity
import com.kutub.smarterp.data.model.SaleItemEntity
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PdfGenerator {

    fun generateInvoicePdf(
        context: Context,
        sale: SaleEntity,
        items: List<SaleItemEntity>,
        currency: String = "$"
    ): File? {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 size
        val page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas

        // Paints
        val titlePaint = Paint().apply {
            color = Color.BLACK
            textSize = 24f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
        }
        val headerPaint = Paint().apply {
            color = Color.DKGRAY
            textSize = 14f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        }
        val boldPaint = Paint().apply {
            color = Color.BLACK
            textSize = 12f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        val textPaint = Paint().apply {
            color = Color.BLACK
            textSize = 12f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        }
        val tableHeaderPaint = Paint().apply {
            color = Color.WHITE
            textSize = 12f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        val tableHeaderBgPaint = Paint().apply {
            color = Color.parseColor("#3F51B5") // Primary color roughly
            style = Paint.Style.FILL
        }

        var yPos = 50f
        val startX = 50f
        val endX = pageInfo.pageWidth - 50f
        val center = pageInfo.pageWidth / 2f

        // Title
        canvas.drawText("Smart Erp", center, yPos, titlePaint)
        yPos += 30f
        
        titlePaint.textSize = 16f
        canvas.drawText("INVOICE", center, yPos, titlePaint)
        yPos += 50f

        // Invoice Info
        val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        canvas.drawText("Invoice #: ${sale.id}", startX, yPos, boldPaint)
        canvas.drawText("Date: ${dateFormat.format(Date(sale.saleDate))}", endX - 150f, yPos, textPaint)
        yPos += 30f

        // Table Header
        canvas.drawRect(startX, yPos, endX, yPos + 30f, tableHeaderBgPaint)
        val textY = yPos + 20f
        canvas.drawText("Item", startX + 10f, textY, tableHeaderPaint)
        canvas.drawText("Qty", startX + 250f, textY, tableHeaderPaint)
        canvas.drawText("Price", startX + 330f, textY, tableHeaderPaint)
        canvas.drawText("Total", startX + 430f, textY, tableHeaderPaint)
        
        yPos += 40f

        // Table Items
        var subTotal = 0.0
        for (item in items) {
            canvas.drawText(item.productName, startX + 10f, yPos, textPaint)
            canvas.drawText(item.quantity.toString(), startX + 250f, yPos, textPaint)
            canvas.drawText("${currency}${item.unitPrice}", startX + 330f, yPos, textPaint)
            val total = item.quantity * item.unitPrice
            subTotal += total
            canvas.drawText("${currency}${total}", startX + 430f, yPos, textPaint)
            
            yPos += 30f
            
            // New page if needed
            if (yPos > pageInfo.pageHeight - 100f) {
                pdfDocument.finishPage(page)
                // Add new page logic here if items are too many (simplified for now)
            }
        }

        yPos += 20f
        // Divider
        canvas.drawLine(startX, yPos, endX, yPos, Paint().apply { color = Color.LTGRAY; strokeWidth = 1f })
        yPos += 30f

        // Totals
        canvas.drawText("Subtotal:", startX + 330f, yPos, boldPaint)
        canvas.drawText("${currency}${subTotal}", startX + 430f, yPos, textPaint)
        yPos += 20f

        if (sale.discount > 0) {
            canvas.drawText("Discount:", startX + 330f, yPos, boldPaint)
            canvas.drawText("-${currency}${sale.discount}", startX + 430f, yPos, textPaint)
            yPos += 20f
        }

        canvas.drawText("Grand Total:", startX + 330f, yPos, boldPaint)
        titlePaint.textSize = 14f
        titlePaint.textAlign = Paint.Align.LEFT
        canvas.drawText("${currency}${sale.finalAmount}", startX + 430f, yPos, titlePaint)
        
        yPos += 50f
        
        // Footer
        titlePaint.textAlign = Paint.Align.CENTER
        titlePaint.textSize = 12f
        titlePaint.color = Color.GRAY
        canvas.drawText("Thank you for your business!", center, yPos, titlePaint)

        pdfDocument.finishPage(page)

        // Save to file
        return try {
            val invoiceDir = File(context.cacheDir, "invoices")
            if (!invoiceDir.exists()) {
                invoiceDir.mkdirs()
            }
            val file = File(invoiceDir, "Invoice_${sale.id}.pdf")
            pdfDocument.writeTo(FileOutputStream(file))
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            pdfDocument.close()
        }
    }
}



