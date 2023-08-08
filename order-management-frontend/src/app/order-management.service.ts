import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class OrderManagementService {

  constructor(private httpClient: HttpClient) { }

  getAllOrders() {
    return this.httpClient.get<OrderDto[]>('http://localhost:8080/api/orders');
  }
}


export class OrderDto {
  customerEmailAddress: string = "";
  customerName: string = "";
  reference: string = "";
  deliveryDate: Date = new Date();

}
