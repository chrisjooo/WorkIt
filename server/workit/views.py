from django.shortcuts import render
from rest_framework.decorators import api_view
from rest_framework import viewsets
from rest_framework.response import Response
from rest_framework import status
from django.contrib.auth.models import User, Group

from workit.serializers import UserSerializer, GroupSerializer
from workit.serializers import ExerciseSerializer, BiodataSerializer
from workit.models import Exercise, Biodata


class UserViewSet(viewsets.ModelViewSet):
    """
    API endpoint that allows users to be viewed or edited.
    """
    queryset = User.objects.all().order_by('-date_joined')
    serializer_class = UserSerializer


class GroupViewSet(viewsets.ModelViewSet):
    """
    API endpoint that allows groups to be viewed or edited.
    """
    queryset = Group.objects.all()
    serializer_class = GroupSerializer

@api_view(['GET'])
def getCaloryPushup(request):
    cal = 0.00848
    weight = int(request.GET.get('weight'))
    total = int(request.GET.get('total'))
    burned = weight*total*cal

    data = {
        'calories_total': str(burned),
        'workout_type' : 'Push-up',
        'weight' : str(weight),
        'amount_of_workout' : str(total)
    }

    return Response(data=data,status=status.HTTP_200_OK)

@api_view(['GET'])
def getCalorySitup(request):
    cal = 0.0059
    weight = int(request.GET.get('weight'))
    total = int(request.GET.get('total'))
    burned = weight*total*cal

    data = {
        'calories_total': str(burned),
        'workout_type' : 'Sit-up',
        'weight' : str(weight),
        'amount_of_workout' : str(total)
    }

    return Response(data=data,status=status.HTTP_200_OK)

@api_view(['GET'])
def getCaloryJogging(request):
    cal = 0.29545
    weight = int(request.GET.get('weight'))
    total = int(request.GET.get('total'))
    burned = weight*total*cal

    data = {
        'calories_total': str(burned),
        'workout_type' : 'Jogging',
        'weight' : str(weight),
        'amount_of_workout' : str(total)
    }

    return Response(data=data,status=status.HTTP_200_OK)
