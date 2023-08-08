import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {OrderDto, OrderManagementService} from '../../../order-management.service';

@Component({
  selector: 'app-order-detail-dialog',
  templateUrl: './order-detail-dialog.component.html',
  styleUrls: ['./order-detail-dialog.component.css']
})
export class OrderDetailDialogComponent {

  constructor(public dialogRef: MatDialogRef<OrderDetailDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: { order: OrderDto },
              private orderManagementService: OrderManagementService) {}

  onClickCancel() {
    this.dialogRef.close();
  }

  downloadFiles() {
    this.orderManagementService.downloadFiles(this.data.order.deliveryDate, this.data.order.customerEmailAddress)
      .subscribe(result => {
        const link = document.createElement('a');
        link.href = URL.createObjectURL(result);
        link.download = this.data.order.customerEmailAddress + '-' + this.data.order.deliveryDate + '.zip';
        link.click();
      });
  }

  onClickDelete() {
    this.orderManagementService.deleteOrder(this.data.order.customerEmailAddress, this.data.order.deliveryDate).subscribe();
    this.dialogRef.close();
  }
}
