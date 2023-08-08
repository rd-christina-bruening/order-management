import {Component, OnInit} from '@angular/core';
import {OrderDto, OrderManagementService} from '../order-management.service';
import {MatDialog} from '@angular/material/dialog';
import {CreateNewOrderDialog} from './create-new-order-dialog/create-new-order-dialog.component';

export interface OrderData {
  customerEmailAddress: string;
  customerName: string;
  reference: string;
  deliveryDate: Date;
}


@Component({
  selector: 'app-order-management-dashboard',
  templateUrl: './order-management-dashboard.component.html',
  styleUrls: ['./order-management-dashboard.component.css']
})
export class OrderManagementDashboardComponent implements OnInit {

  orders: OrderDto[] = [];

  constructor(private orderManagementService: OrderManagementService, public dialog: MatDialog) {
  }

  loadOrders() {
    this.orderManagementService.getAllOrders().subscribe(orders => this.orders = orders);
  }

  ngOnInit(): void {
    this.loadOrders();
  }

  openCreateOrderDialog() {
    const dialogRef = this.dialog.open(CreateNewOrderDialog);

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      this.loadOrders();
    });
  }

}
